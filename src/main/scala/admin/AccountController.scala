package admin

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext.Implicits.global

class AccountController @Inject()(cc: ControllerComponents, authService: AuthService) extends AbstractController(cc) {

  implicit val accountFormat = Json.format[Account]

  def createAccount: Action[Account] = Action.async(parse.json[Account]) { request =>
    val account = request.body
    val newAccount = Account(None, account.username, account.password)
    authService.handleCreateAccount(newAccount). map {
      case Some(_) => Ok("Account is created.")
      case None => Conflict("Username already exists.")
    }
  }

  def login: Action[Account] = Action.async(parse.json[Account]) { request =>
    val account = request.body
    authService.handleLogin(account.username, account.password).map {
      case LoginSuccess => Ok("Login is successful.")
      case LoginFailed => Unauthorized("Invalid username or password.")
    }
  }
}