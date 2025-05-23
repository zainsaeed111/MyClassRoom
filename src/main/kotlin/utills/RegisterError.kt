package utills

sealed class RegisterError(val message: String) {
    data object EmailEmpty : RegisterError("Email cannot be empty")
    data object EmailExists : RegisterError("Email already exists")
    data object InvalidRole : RegisterError("User role cannot be null")
    data object DatabaseError : RegisterError("Failed to create user")
}