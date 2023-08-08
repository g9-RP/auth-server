import admin._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.mockito.Mockito._

class AuthServiceSpec extends AnyFunSpec with MockitoSugar {

  describe("User creation") {
    it("should create user if user doesn't exist") {
      val username = "user123"
      val password = "password123"
      val newAccount = Account(None, username, password)

      val mockAccountRepo = mock[AccountRepository]
      when(mockAccountRepo.getUser(username)).thenReturn(Future.successful(None))
      when(mockAccountRepo.createUser(newAccount)).thenReturn(Future.successful(newAccount))

      val authService = new AuthService(mockAccountRepo)
      val result = authService.handleCreateAccount(newAccount)

      result shouldEqual Future(Some(newAccount))
    }

    it("should fail to create user if user already exists") {
      val username = "user123"
      val password = "password123"
      val newAccount = Account(None, username, password)

      val mockAccountRepo = mock[AccountRepository]
      when(mockAccountRepo.getUser(username)).thenReturn(Future.successful(Some(newAccount)))
      when(mockAccountRepo.createUser(newAccount)).thenReturn(Future.successful(newAccount))

      val authService = new AuthService(mockAccountRepo)
      val result = authService.handleCreateAccount(newAccount)

      result shouldEqual Future(None)
    }
  }

  describe("User login") {
    it("should login if user exists and password is valid") {
      val username = "user123"
      val password = "password123"
      val newAccount = Account(None, username, password)

      val mockAccountRepo = mock[AccountRepository]
      when(mockAccountRepo.getUser(username)).thenReturn(Future.successful(Some(newAccount)))
      when(mockAccountRepo.createUser(newAccount)).thenReturn(Future.successful(newAccount))

      val authService = new MockAuthService(mockAccountRepo)
      authService.setIsValidPassword(true)
      val result = authService.handleLogin(username, password)

      result shouldEqual Future(LoginSuccess)
    }

    it("should fail to login if username is invalid") {
      val username = "user123"
      val password = "password123"
      val newAccount = Account(None, username, password)

      val mockAccountRepo = mock[AccountRepository]
      when(mockAccountRepo.getUser(username)).thenReturn(Future.successful(None))
      when(mockAccountRepo.createUser(newAccount)).thenReturn(Future.successful(newAccount))

      val authService = new MockAuthService(mockAccountRepo)
      authService.setIsValidPassword(false)
      val result = authService.handleLogin(username, password)

      result shouldEqual Future(LoginFailed)
    }

    it("should fail to login if password is invalid") {
      val username = "user123"
      val password = "password123"
      val account = Account(None, username, password)

      val mockAccountRepo = mock[AccountRepository]
      when(mockAccountRepo.getUser(username)).thenReturn(Future.successful(Some(account)))
      when(mockAccountRepo.createUser(account)).thenReturn(Future.successful(account))

      val authService = new MockAuthService(mockAccountRepo)
      authService.setIsValidPassword(false)
      val result = authService.handleLogin(username, password)

      result shouldEqual Future(LoginFailed)
    }
  }

  class MockAuthService(accountRepository: AccountRepository) extends AuthService(accountRepository) {
    private var isValidPassword = false
    override def isPasswordValid(password: String, hashedPassword: String): Boolean = isValidPassword
    def setIsValidPassword(isValid: Boolean): Unit = {
      isValidPassword = isValid
    }
  }
}