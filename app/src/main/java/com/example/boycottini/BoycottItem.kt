package com.example.boycottini

data class BoycottItem(
    val id: Long,
    val name: String = "",
    val imageUrl: String = "",
    val alternative: String = "",
    val alternativeSourceLink: String = "",
    val raison: String = "",
    val barcode: Long = 0,
    val brand: String = ""
) {
    // Secondary Constructor - only provides id, name, and imageUrl
    constructor(id: Long, name: String, imageUrl: String) : this(
        id = id,
        name = name,
        imageUrl = imageUrl,
        alternative = "",  // default value
        alternativeSourceLink = "",  // default value
        raison = "",  // default value
        barcode = 0,  // default value
        brand = ""  // default value
    )
    constructor(id: Long, name: String, imageUrl: String,brand:String) : this(
        id = id,
        name = name,
        imageUrl = imageUrl,
        alternative = "",  // default value
        alternativeSourceLink = "",  // default value
        raison = "",  // default value
        barcode = 0,  // default value
        brand = brand  // default value
    )


}

