package utils

import org.mindrot.jbcrypt.BCrypt
import java.util.regex.Pattern

fun String.isValidMail(): Boolean {
    return "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex().matches(this)
}

fun String.isValidPassword(): Boolean {
    // Password should be minimum 8 characters long
    if (this.length < 8) {
        return false
    }

    // Password should contain at least one capital letter
    var exp = ".*[A-Z].*"
    var pattern = Pattern.compile(exp)
    var matcher = pattern.matcher(this)
    if (!matcher.matches()) {
        return false
    }

    // Password should contain at least one small letter
    exp = ".*[a-z].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(this)
    if (!matcher.matches()) {
        return false
    }

    // Password should contain at least one number
    exp = ".*[0-9].*"
    pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
    matcher = pattern.matcher(this)
    if (!matcher.matches()) {
        return false
    }

    return true
}

fun String.hashPassword(): String {
    return BCrypt.hashpw(this, BCrypt.gensalt())
}

fun String.isValidUsername(): Boolean {
    return "^[A-Ža-ž0-9][A-Ža-ž\\s0-9\\-\\.]{2,}".toRegex().matches(this)
}

fun String.isValidCoordinates(): Boolean {
    return "[0-9]{1,2}\\.[0-9]{5,7}\\s[0-9]{1,2}\\.[0-9]{5,7}".toRegex().matches(this)
}
