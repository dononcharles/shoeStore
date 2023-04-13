package com.udacity.shoestore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Komi Donon
 * @since 4/10/2023
 */
@Parcelize
data class Shoe(
    var name: String,
    var size: Double,
    var company: String,
    var description: String,
    val images: List<String> = mutableListOf(),
) : Parcelable {

    companion object {
        val defaultShoeList = mutableListOf(
            Shoe(
                name = "Bend Low",
                size = 46.00,
                company = "Naturleder",
                description = "Der lässige BIRKENSTOCK Sneaker Bend überzeugt durch seine Schlichtheit und ist vielseitig kombinierbar",
            ),
            Shoe(
                name = "QO 500",
                size = 35.00,
                company = "Naturleder",
                description = "Der Schuh ist nach EN ISO 20347:2012 zertifiziert und erfüllt die Anforderungen der Schutzklasse O2.",
            ),
        )
    }
}
