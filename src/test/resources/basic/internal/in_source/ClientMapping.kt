package basic.`internal`.in_source

public fun Client.toUser(): User =
    User().also {
        it.age = age.value
    }