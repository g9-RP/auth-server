package admin

import play.api.ApplicationLoader.Context
import play.api._
import play.filters.HttpFiltersComponents
import router.Routes

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment)
    }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
        with HttpFiltersComponents {

  val accountRepository = new AccountRepository
  val accountService = new AuthService(accountRepository)
  lazy val accountController = new AccountController(controllerComponents, accountService)

  override def router: Routes = new Routes(httpErrorHandler, accountController)
}
