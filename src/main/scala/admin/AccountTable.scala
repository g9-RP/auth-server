package admin

import slick.jdbc.PostgresProfile.api._

class AccountTable(tag: Tag) extends Table[Account](tag, "accounts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username", O.Unique)
  def password = column[String]("password")

  def * = (id.?, username, password) <> (Account.tupled, Account.unapply)
}

object AccountTable {
  val tableQuery = TableQuery[AccountTable]
}