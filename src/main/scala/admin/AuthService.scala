package admin

import scala.concurrent.{ExecutionContext, Future}


class AuthService(accountRepository: AccountRepository)(implicit ec: ExecutionContext) {

  def handleCreateAccount(account: Account)(implicit ec: ExecutionContext): Future[Option[Account]] = {
    // Check if the account already exists
    accountRepository.getUser(account.username).flatMap {
      case Some(_) => Future.successful(None)
      case None =>
        val hashedPassword = PasswordHasher.hash(account.password)
        accountRepository.createUser(account).map(Some(_))
    }
  }

  def handleLogin(username: String, password: String)(implicit ec: ExecutionContext): Future[LoginResult] = {
    accountRepository.getUser(username).map {
      case Some(user) =>
        // Check the password the hashed password stored in db
        if (isPasswordValid(password, user.password)){
          LoginSuccess
        } else {
          LoginFailed
        }
      case None => LoginFailed
    }
  }

  // Extracted out for unit test
  protected def isPasswordValid(password: String, hashedPassword: String): Boolean = {
    PasswordHasher.checkPassword(password, hashedPassword)
  }
}