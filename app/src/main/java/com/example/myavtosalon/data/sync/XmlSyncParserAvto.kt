package com.example.myavtosalon.data.sync

import android.content.Context
import android.util.Xml
import com.example.myavtosalon.R
import com.example.myavtosalon.data.local.Brand
import com.example.myavtosalon.data.local.CarModel
import com.example.myavtosalon.data.local.Client
import org.xmlpull.v1.XmlPullParser

data class AvtoSyncPayload(
    val brands: List<Brand>,
    val models: List<CarModel>,
    val clients: List<Client>
)

object XmlSyncParserAvto {

    fun parse(context: Context): AvtoSyncPayload {
        val brands = mutableListOf<Brand>()
        val models = mutableListOf<CarModel>()
        val clients = mutableListOf<Client>()

        val parser = Xml.newPullParser()
        context.resources.openRawResource(R.raw.sync_data).use { input ->
            parser.setInput(input, "UTF-8")

            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    when (parser.name) {
                        "brand" -> {
                            val id = parser.getAttributeValue(null, "id").toLong()
                            val name = parser.getAttributeValue(null, "name") ?: ""
                            brands.add(Brand(id = id, name = name))
                        }

                        "model" -> {
                            val id = parser.getAttributeValue(null, "id").toLong()
                            val brandId = parser.getAttributeValue(null, "brandId").toLong()
                            val name = parser.getAttributeValue(null, "name") ?: ""
                            val bodyType = parser.getAttributeValue(null, "bodyType")
                            val priceStr = parser.getAttributeValue(null, "price")
                            val price = priceStr?.toDoubleOrNull()

                            models.add(
                                CarModel(
                                    id = id,
                                    name = name,
                                    brandId = brandId,
                                    bodyType = bodyType,
                                    price = price
                                )
                            )
                        }

                        "client" -> {
                            val id = parser.getAttributeValue(null, "id").toLong()
                            val modelId = parser.getAttributeValue(null, "modelId").toLong()
                            val fullName = parser.getAttributeValue(null, "fullName") ?: ""
                            val phone = parser.getAttributeValue(null, "phone") ?: ""

                            clients.add(
                                Client(
                                    id = id,
                                    fullName = fullName,
                                    phone = phone,
                                    modelId = modelId
                                )
                            )
                        }
                    }
                }
                event = parser.next()
            }
        }

        return AvtoSyncPayload(brands, models, clients)
    }
}
