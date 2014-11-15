package controllers

import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.libs.json.Json
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.Play.current

object AppController extends Controller {

  def index = TODO
  
  def about = TODO
  
  def add = TODO
  
  def feed = TODO
  
  def map = TODO
  
  def myPoops = TODO
  
  def poop(id: Long) = TODO
  
  def apiAdd = TODO
  
  def apiNearbyPoops = TODO
  
  def apiMyPoops = TODO
}