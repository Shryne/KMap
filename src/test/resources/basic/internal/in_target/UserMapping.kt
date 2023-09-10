package basic.`internal`.in_target

public fun User.toClient(): Client =
    Client().also {
        it.age = age.value
    }