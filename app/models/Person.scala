package models

case class Person(age:Int, name:Name, bloodType:Option[String], favoriteNumbers:Seq[Int]);

case class Name(first:String, last:String);
