package models

import java.sql.Timestamp

import java.util.Random
import scala.slick.driver.H2Driver.simple._
import play.api.Play.current
import play.api.libs.json._


class Poop(tag: Tag) extends Table[(Long, String, Double, Double, Int, Int, Int, Int, Int, String, Timestamp)](tag, "POOP") {
  def id = column[Long]("POOP_ID", O.PrimaryKey, O.AutoInc)
  def locName = column[String]("LOC_NAME")
  def longitude = column[Double]("LONGITUDE")
  def latitude = column[Double]("LATITUDE")
  // white on black styling, black on white is default.
  def experience = column[Int]("EXPERIENCE")
  def cleanliness = column[Int]("CLEANLINESS")
  def facilities = column[Int]("FACILITIES")
  def atmosphere = column[Int]("ATMOSPHERE")
  def poopRating = column[Int]("POOP_RATING")
  def notes = column[String]("NOTES")
  def time = column[Timestamp]("TIME")
  
  def * = (id, locName, longitude, latitude, experience, cleanliness,
		  facilities, atmosphere, poopRating, notes, time)
  
  def available = time > Poop.availablePast
}

object Poop {
  type PoopTuple = (Long, String, Double, Double, Int, Int, Int, Int, Int, String, Timestamp)
  
  def poops = TableQuery[Poop]
  
  def now = new Timestamp(System.currentTimeMillis)
  
  def availablePast = {
    val fifteenMinutes: Long = 60 * 15
    new Timestamp(System.currentTimeMillis - fifteenMinutes)
  }
  
  def create = {
    val maybeUrl = current.configuration.getString("db.default.url")
    val url = maybeUrl getOrElse {throw new Exception("Bad database config")}
    val ref = Database.forURL(url)
    
    ref.withSession { implicit s => poops.ddl.create }
  }
  
  def nearby(lat2: Double, long2: Double)(implicit session: Session): List[PoopTuple] = {
    val poopList = poops.list
    poopList filter { p => 
      val lat1 = p._4
      val long1 = p._3
      nearbyDoubles(lat1, long1, lat2, long2)
    }
  }
  
  def nearbyDoubles(lat1: Double, long1: Double, 
		  			lat2: Double, long2: Double): Boolean = {
    val R = 6371
    val (a1, a2, o1, o2) = (lat1.toRadians, lat2.toRadians, long1.toRadians, long2.toRadians)
    println(a1 + "|" + o1 + "|" + a2 + "|" + o2 )
    val aDiff = (a1 - a2).toRadians
    val oDiff = (o1 - o2).toRadians
    val a = Math.sin(aDiff/2) * Math.sin(aDiff/2) +
    		Math.cos(a1) * Math.cos(a2) *
    		Math.sin(oDiff/2) * Math.sin(oDiff/2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val d = R * c
    d < 0.15
  }
  
  def timeAgo(poople: PoopTuple): String = {
    val time = poople._11
    val millisAgo = now.getTime() - time.getTime()
    val secondsAgo: Int = (millisAgo / 1000).toInt
    val minutesAgo = secondsAgo / 60;
    val hoursAgo = minutesAgo / 60
    val daysAgo = hoursAgo / 24
    if(minutesAgo < 60) {
      minutesAgo + " min(s) ago"
    } else if(hoursAgo < 24) {
      if(hoursAgo == 1) {
        hoursAgo + " hour ago"
      } else {
        hoursAgo + " hours ago"
      }
    } else {
      if(daysAgo == 1) {
        daysAgo + " day ago"
      } else {
        daysAgo + " days ago"
      }
    }
  }
  
  def poopleToJson(poople: PoopTuple): JsValue = {
    val json: JsValue = JsObject(Seq(
        "location" -> JsString(poople._2),
        "longitude" -> JsNumber(poople._3),
        "latitude" ->  JsNumber(poople._4),
        "experience" -> JsNumber(poople._5),
        "cleanliness" -> JsNumber(poople._6),
        "facilities" -> JsNumber(poople._7),
        "atmosphere" -> JsNumber(poople._8),
        "poop" -> JsNumber(poople._9),
        "notes" -> JsString(poople._10),
        "time" -> JsString(timeAgo(poople))
      )
    )
    json
  }
}