package ch.hearc.zoukfiesta

import java.util.ArrayList

object NearbyEndPointStore   {
    val ENDPOINTS: MutableList<NearbyEndpoint> = ArrayList()

//    val GARBAGE_CATEGORIES: MutableList<GarbageCategory> = ArrayList()

    init {
        // Let's create some data on the fly...
//        val categoryPet = GarbageCategory("PET", "PET recycle bin")
//        val categoryIron = GarbageCategory("Iron", "Iron container")
//        val categoryAluminum = GarbageCategory("Aluminum", "Aluminum container")
//
//        GARBAGE_CATEGORIES.add(categoryPet)
//        GARBAGE_CATEGORIES.add(categoryIron)
//        GARBAGE_CATEGORIES.add(categoryAluminum)
//
//        GARBAGES.add(Garbage("Valser Classic", categoryPet))
//        GARBAGES.add(Garbage("Lipton Lemon Ice Tea", categoryPet))
//        GARBAGES.add(Garbage("Coca Cola", categoryPet))
//
//        GARBAGES.add(Garbage("Canned food", categoryIron))
//        GARBAGES.add(Garbage("Pot lid", categoryIron))
//        GARBAGES.add(Garbage("Canned sardines", categoryIron))
//
//        GARBAGES.add(Garbage("Yogurt lid", categoryAluminum))
//        GARBAGES.add(Garbage("Knife blade", categoryAluminum))
//        GARBAGES.add(Garbage("Aluminum foil", categoryAluminum))
    }

    /**
     * Utility function to find a garbage by its name.
     */
    fun findEndPointByName(id: String): NearbyEndpoint? {
        var result: NearbyEndpoint? = null

        for (endpoint in ENDPOINTS) {
            if (endpoint.id == id) {
                result = endpoint
            }
        }

        return result
    }
}