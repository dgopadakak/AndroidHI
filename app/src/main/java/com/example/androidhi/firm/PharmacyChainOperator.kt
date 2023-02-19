package com.example.androidhi.firm

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androidhi.firm.dbWithRoom.PharmacyChainOperatorConverter
import java.util.*
import kotlin.collections.ArrayList

@Entity
class PharmacyChainOperator()
{
    @PrimaryKey
    private var id: Int = 1

    @TypeConverters(PharmacyChainOperatorConverter::class)
    private var pharmacyChains: ArrayList<PharmacyChain> = ArrayList()

    fun getPharmacyChains(): ArrayList<PharmacyChain>
    {
        return pharmacyChains
    }

    fun setPharmacyChains(newPharmacyChains: ArrayList<PharmacyChain>)
    {
        pharmacyChains = newPharmacyChains
    }

    fun setId(id: Int)
    {
        this.id = id
    }

    fun getId(): Int
    {
        return id
    }

    fun getPharmaciesNames(indexGroup: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in pharmacyChains[indexGroup].listOfPharmacies)
        {
            arrayListForReturn.add(i.nameOfPharmacy)
        }
        return arrayListForReturn
    }

    fun getPharmaciesAddresses(indexGroup: Int): ArrayList<String>
    {
        val arrayListForReturn: ArrayList<String> = ArrayList()
        for (i in pharmacyChains[indexGroup].listOfPharmacies)
        {
            arrayListForReturn.add(i.address)
        }
        return arrayListForReturn
    }

    fun getPharmaciesNumbers(indexGroup: Int): ArrayList<Int>
    {
        val arrayListForReturn: ArrayList<Int> = ArrayList()
        for (i in pharmacyChains[indexGroup].listOfPharmacies)
        {
            arrayListForReturn.add(i.num)
        }
        return arrayListForReturn
    }

    fun getPharmacy(indexGroup: Int, indexExam: Int): Pharmacy
    {
        return pharmacyChains[indexGroup].listOfPharmacies[indexExam]
    }

    fun sortPharmacy(indexGroup: Int, sortIndex: Int)
    {
        if (sortIndex == 0)
        {
            val tempArrayListOfExamsNames: ArrayList<String> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfExamsNames.add(i.nameOfPharmacy.lowercase(Locale.ROOT))
            }
            tempArrayListOfExamsNames.sort()
            for (i in tempArrayListOfExamsNames)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.nameOfPharmacy.lowercase(Locale.ROOT)
                        && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 1)
        {
            val tempArrayListOfTeacherNames: ArrayList<String> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfTeacherNames.add(i.address.lowercase(Locale.ROOT))
            }
            tempArrayListOfTeacherNames.sort()
            for (i in tempArrayListOfTeacherNames)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.address.lowercase(Locale.ROOT)
                        && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 2)
        {
            val tempArrayListOfAuditory: ArrayList<Int> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfAuditory.add(i.num)
            }
            tempArrayListOfAuditory.sort()
            for (i in tempArrayListOfAuditory)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.num && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 3)
        {
            val tempArrayListOfTeacherNames: ArrayList<String> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfTeacherNames.add(i.timeOpen.lowercase(Locale.ROOT))
            }
            tempArrayListOfTeacherNames.sort()
            for (i in tempArrayListOfTeacherNames)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.timeOpen.lowercase(Locale.ROOT)
                        && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 4)
        {
            val tempArrayListOfTeacherNames: ArrayList<String> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfTeacherNames.add(i.timeClose.lowercase(Locale.ROOT))
            }
            tempArrayListOfTeacherNames.sort()
            for (i in tempArrayListOfTeacherNames)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.timeClose.lowercase(Locale.ROOT)
                        && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 5)
        {
            val tempArrayListOfPeoples: ArrayList<Int> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfPeoples.add(i.parkingSpaces)
            }
            tempArrayListOfPeoples.sort()
            for (i in tempArrayListOfPeoples)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.parkingSpaces && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 6)
        {
            val tempArrayListOfAbstract: ArrayList<Int> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfAbstract.add(i.isDelivery)
            }
            tempArrayListOfAbstract.sort()
            for (i in tempArrayListOfAbstract)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.isDelivery && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }

        if (sortIndex == 7)
        {
            val tempArrayListOfComment: ArrayList<String> = ArrayList()
            val tempArrayListOfPharmacies: ArrayList<Pharmacy> = ArrayList()
            for (i in pharmacyChains[indexGroup].listOfPharmacies)
            {
                tempArrayListOfComment.add(i.comment.lowercase(Locale.ROOT))
            }
            tempArrayListOfComment.sort()
            for (i in tempArrayListOfComment)
            {
                for (j in pharmacyChains[indexGroup].listOfPharmacies)
                {
                    if (i == j.comment.lowercase(Locale.ROOT)
                        && !tempArrayListOfPharmacies.contains(j))
                    {
                        tempArrayListOfPharmacies.add(j)
                        break
                    }
                }
            }
            pharmacyChains[indexGroup].listOfPharmacies = tempArrayListOfPharmacies
        }
    }
}