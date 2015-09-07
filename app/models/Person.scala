package models

/**
 * Created by a-nakamura on 2015/09/04.
 */

case class Person(age:Int, name:Name, bloodType:Option[String], favoriteNum:Seq[Int]);

case class Name(first:String, last:String);
