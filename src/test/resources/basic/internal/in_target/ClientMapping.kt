package basic.`internal`.in_target

public fun Client.toUser(): User =
    User().also {
        it.age.value = age
    }