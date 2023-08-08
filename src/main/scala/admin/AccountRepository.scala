package admin

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future

class AccountRepository {
  val database = Database.forConfig("database")
  val accounts = TableQuery[AccountTable]

  def createUser(user: Account): Future[Account] = {
    database.run(accounts returning accounts += user)
  }

  def getUser(username: String): Future[Option[Account]] = {
    database.run(accounts.filter(_.username === username).result.headOption)
  }
}