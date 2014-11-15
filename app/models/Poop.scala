package models

import java.sql.Timestamp

import java.util.Random
import scala.slick.driver.H2Driver.simple._

class Poop(tag: Tag) extends Table[(Long, String, Double, Double, Int, Int, Int, Int, Int, Timestamp)](tag, "POOP") {
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
  def time = column[Timestamp]("TIME")
  
  def * = (id, locName, longitude, latitude, experience,
		   cleanliness, facilities, atmosphere, poopRating, time)
  
  def available = time > Poop.availablePast
}

object Poop {
  def poops = TableQuery[Poop]
  
  def availablePast = {
    val fifteenMinutes: Long = 60 * 15
    new Timestamp(System.currentTimeMillis - fifteenMinutes)
  }
}