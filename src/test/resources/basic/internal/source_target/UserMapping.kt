package basic.`internal`.source_target

public fun User.toClient(): Client =
    Client().also {
        it.age.value = age.value
    }