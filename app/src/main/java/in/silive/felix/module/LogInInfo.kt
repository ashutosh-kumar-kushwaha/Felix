package `in`.silive.felix.module

data class LogInInfo (
    val token : String,
    val logInState : Boolean,
    val name : String,
    val email : String,
    val role : String
)