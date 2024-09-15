//package com.lovinsharma.kalanikethancic.model
//import java.util.regex.Pattern
//
//
//class Validation(message: String) : Exception(message)
//
//
//
//fun validateParents(parents: List<Parents>) {
//    if (parents.size > 3) {
//        throw Validation("A student can have a maximum of 3 parents. Provided: ${parents.size}.")
//    }
//}
//
//fun validateNumber(number: Int) {
//    if (number.toString().length != 11 || !number.toString().startsWith("0")) {
//        throw Validation("Invalid number, must start with '0' and have 11 digits. Provided: $number")
//    }
//}
//
//fun validateEmail(email: String) {
//    if (!isValidEmail(email)) {
//        throw Validation("Invalid Email")
//    }
//}
//
//// Function to validate email address
//fun isValidEmail(email: String): Boolean {
//    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
//    val pattern = Pattern.compile(emailRegex)
//    return pattern.matcher(email).matches()
//}