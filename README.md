# KMap
KMap is a generator. It generates mappers based on annotations found on the classes that need these mappings. 
Take for example these two classes:

<style>
tr, th, td { border: none!important; }
</style>
<table>
<tr><td>

```kotlin
@MapPartner
class Size {
    @KMap var w: Int = 0
    @KMap var h: Int = 0
}
```

</td>
<td>

```kotlin
class Dimension {
    var w: Int = 0
    var h: Int = 0
}
```

</td></tr></table>

This will generate these files (they won't look exactly like this):

<table>
<tr><td>

```kotlin
fun Size.toDimension() = 
    Dimension().also {
        it.w = w
        it.h = h
    }
```

</td>
<td>

```kotlin
fun Dimension.toSize() =
    Size().also {
        it.w = w
        it.h = h
    }
```

</td></tr></table>

Which is very convenient to call:
```kotlin
Size(w = 15, h = 284).toDimension() // => Dimension(w = 15, h = 284)
```

