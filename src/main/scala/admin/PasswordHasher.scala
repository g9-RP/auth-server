package admin

import org.mindrot.jbcrypt.BCrypt

/**
  * Using BCrypt hashing function to handle both the hashing of the password and password verification
  */
object PasswordHasher {
  def hash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

  def checkPassword(password: String, hashedPassword: String): Boolean = BCrypt.checkpw(password, hashedPassword)
}
