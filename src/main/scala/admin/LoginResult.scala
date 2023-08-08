package admin

// Using the following objects to represent login sucess and login failure.
sealed trait LoginResult
object LoginSuccess extends LoginResult
object LoginFailed extends LoginResult