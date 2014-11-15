package controllers

import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.libs.json._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.Play.current

import models.Poop

object AppController extends Controller {
  
  def create = Action { Poop.create; Ok("hi")}

  def index() = Action { Redirect(controllers.routes.AppController.map) }
  
  def about = TODO
  
  def add = Action { 
    Ok(views.html.add())
  }
  
  def feed = DBAction { implicit rs =>
    val len = Poop.poops.list.length
    Ok(len.toString)
  }
  
  def map = Action {
    Ok(views.html.map())
  }
  
  def myPoops = TODO
  
  def poop(id: Long) = TODO
  
  def apiAdd() = DBAction { implicit rs =>
    val body = rs.body.asFormUrlEncoded.getOrElse(Map())
    val loc = body("location").headOption getOrElse ""
    val lat = body("latitude").headOption getOrElse ""
    val long = body("longitude").headOption getOrElse ""
    val exp = body("experience").headOption getOrElse ""
    val clean = body("cleanliness").headOption getOrElse ""
    val fac = body("facilities").headOption getOrElse ""
    val atmo = body("atmosphere").headOption getOrElse ""
    val poop = body("poop").headOption getOrElse ""
    val notes = body("notes").headOption getOrElse ""
    
    def getDouble(str: String): Double = java.lang.Double.parseDouble(str)
    
    def getInt(str: String): Int = Integer.parseInt(str)
    
    Poop.poops += (0, loc, getDouble(lat), getDouble(long),
    	getInt(exp), getInt(clean), getInt(fac), getInt(atmo),
    	getInt(poop), notes, Poop.now)
    
    Ok("success")
  }
  
  def apiNearbyPoops = DBAction { implicit rs =>
    val body = rs.body.asFormUrlEncoded.getOrElse(Map())
    val lat = java.lang.Double.parseDouble(body("latitude").headOption getOrElse "")
    val long = java.lang.Double.parseDouble(body("longitude").headOption getOrElse "")
    val pooples = Poop.nearby(lat, long)
    val jsArray = JsArray(pooples.toSeq map { Poop.poopleToJson(_) })
    Ok(jsArray)
  }
  
  def apiMyPoops = TODO
}