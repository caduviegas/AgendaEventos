package com.innaval.agendaeventos.data.mapper

import junit.framework.Assert
import org.junit.Rule
import org.junit.Test

class EventsMapperTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `when map EventoDTO should return BO`(){
        val mapped = mockEventPoolParty().toBO()
        Assert.assertEquals("42", mapped.id)
        Assert.assertEquals("Pool Party", mapped.title)
        Assert.assertEquals(
            "https://www.bonde.com.br/img/bondenews/2018/11/img_2842.jpg",
            mapped.imageUrl
        )
        Assert.assertEquals(49.9, mapped.price)
        Assert.assertEquals(-53.4488875, mapped.longitude)
        Assert.assertEquals(-33.6923026, mapped.latitude)
        Assert.assertEquals(1606262400000, mapped.date.time)
        Assert.assertEquals(null, mapped.people)
}