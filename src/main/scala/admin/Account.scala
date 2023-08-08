package admin

// Id is auto-generated when the record gets stored in db if none is provided
case class Account(id: Option[Long], username: String, password: String)
