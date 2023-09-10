package basic.`internal`.in_source

public fun User.toClient(): Client =
    Client().also {
        it.age.value = age
    }