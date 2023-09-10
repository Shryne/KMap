package basic.`internal`.source_target

public fun Client.toUser(): User =
    User().also {
        it.age.value = age.value
    }