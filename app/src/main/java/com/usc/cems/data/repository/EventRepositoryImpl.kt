package com.usc.cems.data.repository

import com.usc.cems.data.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor() : EventRepository {

    // Initial upcoming events list
    private val upcomingEvents = listOf(
        Event(
            id = "1",
            title = "Intro to UI/UX with Figma",
            category = "Workshop",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuADwKz2uH2H6V2HKSWMXaA_TQ6EVx90P06o5aXAsidYR953YbUl33-1ojwUjbv6tKqe8IqobWimdXrNmKWzKYZmAmCBYFP_SIebaLrvgwaKrCGu0JmyoEPUs0_1WoGBhW-BrmYcLIkiGjg0T8OTKT1qKP1PFiXFa0mUCCR6Z9fHndvGVKKb7_a2MuHLBijTcjh6rNxaMG9nKW6y-bdTXf31se5aoDf9xTHQ_FTdyYz0EV_7-BIwAURx7Q",
            dateTime = "Oct 12 • 2:30 PM - 4:00 PM",
            location = "Engineering Building, Lab 402",
            spotsLeft = "24 spots left",
            description = "Learn the fundamentals of designing clean, interactive user interfaces using Figma. This hands-on workshop covers wireframing, component libraries, typography hierarchy, and prototyping to set you up for your next design projects.",
            organizerName = "Computer Engineering Council",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "24 students are attending",
            registrationStatus = "Open until Oct 12"
        ),
        Event(
            id = "2",
            title = "Varsity Basketball: Finals",
            category = "Sports",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBCQTCcNBF1wOpcIMotc_rjOe1Wi_hASDSx7C9F5Qv7Wj5_aaOe9tHGg1hKNDgIi692VsHTgsyqk5uUpN7ehrTVUeCyedT8yHpjf9m3OiaAZAoSRvl0T3WbFF43Tk0q1KTN7Q-jkIAj2FcqhnuAMd4tUPaWX7sXYuMrktXKPc7cvzDO00XE7WWAnyi5EFRXYbdqslFY6DnW41DCgg73eURZpiBGZam_KRFVPh10x28JBy-EY3TbdDNlmg",
            dateTime = "Oct 14 • 6:00 PM",
            location = "Main Sports Complex",
            spotsLeft = null,
            description = "Get ready for the biggest sports event of the semester! Watch the Carolinian Warriors battle it out in the basketball finals. Bring your school spirit, wear blue and gold, and cheer our team to victory!",
            organizerName = "USC Athletics Office",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "150 students are attending",
            registrationStatus = "Open until Oct 14"
        ),
        Event(
            id = "3",
            title = "International Food Festival",
            category = "Cultural",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDfXIpxvvvQAo3BlRv_6tTRawA7Jqo9rfofDe8cZuV4s4WENfgeuXeEK4TME-P9hMqGIqZtWQwOgcald9_nr2v753ott6XoffJVLaDaLmK_g5VKOFEraYdkwqEhSKnzmvSkKpp0UXRqQa1GTxDGVTodInPhzNKPym0bydsLZj81PYRop5ReUFMFej3ctFu-I_tTNRZmeQtVnIrvqk5llp5AfXfQyIuCZIYFRm1FybIoskqnr3bubaUpPg",
            dateTime = "Oct 15 • 11:00 AM - 3:00 PM",
            location = "Central Campus Green",
            spotsLeft = null,
            description = "Experience cultures and cuisines from around the world! Sample traditional dishes, desserts, and drinks prepared by international students and culinary organizations. Enjoy live cultural performances throughout the day.",
            organizerName = "International Students Association",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "85 students are attending",
            registrationStatus = "Open until Oct 15"
        )
    )

    // Initial completed/past events list
    private val pastEvents = listOf(
        Event(
            id = "past_1",
            title = "Winter Social Mixer",
            category = "Social",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCAlcRTnGcxMg342_FGqC5bOyJesuymAYUs9AHHmNBS0UfPEfGiZzZY0wLjxnsyBAxET5BDlZvaBnE4INtkxH9pEpZx1FbwUf6--pK2cR_ixVMmAdZlmgXJZfPfJUzqfVKUZETL9tVlp7UoDW3-yq_A9AZMNFhjCi33xiqbVUqC1AX63vsr09GRKVipv0K81bGMXLGF03M8u_iKHlQ_9zadO7dgseugk6AUjPGVsxhwmqsbA_Qyfov72g",
            dateTime = "Feb 14, 2024 • 6:30 PM",
            location = "Student Union, Floor 1",
            spotsLeft = null,
            description = "A cozy indoor student mixer with warm string lights, wooden tables, and students laughing together. The scene is intimate and social, with a focus on community building. Soft warm lighting creates a nostalgic and friendly campus atmosphere.",
            organizerName = "USC Student Union Office",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "72 students attended",
            registrationStatus = "Completed"
        ),
        Event(
            id = "past_2",
            title = "Homecoming Game",
            category = "Sports",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB9BVkO39qKXAAzXLrMgaD4ri-KQZRb-aMwBH5DDhPdFN3aR6Mgd1rBqAGDXZ6rtx6Z683ldu7f4NXcc5tyQALsELhRg9G7Te68s4qU9nMsq-527KVfDwAR9AWzfa1LSIfhcMMyZaYYdeRiUHKFQU01QNz4DuHMLroeyUE8ukWXtFrJSCMXbVo-iR7-d-_QpE1hK4JhNgdimdHZR7erwplFtJSMUc9V1ig36gIvMSVs33zLKjnJqdTtXg",
            dateTime = "Jan 28, 2024 • 4:00 PM",
            location = "Arena Sports Complex",
            spotsLeft = null,
            description = "An indoor basketball court during a high-energy university game. The floor is polished wood, reflecting the bright overhead lights. Fans in the background are cheering, and the atmosphere is dynamic and spirited, featuring school colors of blue and gold.",
            organizerName = "USC Athletics Office",
            organizerLogo = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBkuM5btIeSGlZYkviOI_ikadaa7meJOX_vVgO0WFCh5PsjNAAqu5bZsfixtExgIjvBFWz_jS7Q67ardG8KKf-FK4oEZEdzW9ClrnnVFFPhgdelnlE8H6Ul2FeMYCWGilxdj2UU7U1Q_kofBpiY28RqlOuM0rdQYKPxOAdpvj6WTx5EZ3MkAFSUAa7NQQrYYwPXPe7eaGw6wA4BL4Sg_phOxO4WChvmlhNA3v6tdEMBq-jlcDdGeE0FQ",
            attendingCount = "240 students attended",
            registrationStatus = "Completed"
        )
    )

    private val allEvents = MutableStateFlow<List<Event>>(upcomingEvents + pastEvents)

    // Maps userId to a set of registered eventIds
    private val registrations = mutableMapOf<String, MutableSet<String>>()

    init {
        // Pre-register Admin user & default mock student to a few events to populate the lists
        val seededRegistrations = mutableSetOf("1", "2", "past_1", "past_2")
        registrations["admin_uid_seed"] = seededRegistrations.toMutableSet()
        registrations["mock_uid"] = seededRegistrations.toMutableSet()
    }

    override fun getEvents(): StateFlow<List<Event>> {
        // Return only upcoming/active events for the Home Dashboard feed
        return MutableStateFlow(allEvents.value.filter { !it.id.startsWith("past_") }).asStateFlow()
    }

    override fun getEventById(id: String): Event? {
        return allEvents.value.find { it.id == id }
    }

    override suspend fun addEvent(event: Event): Result<Unit> = runCatching {
        val currentList = allEvents.value.toMutableList()
        currentList.add(0, event)
        allEvents.value = currentList
    }

    // Registrations logic
    override fun getRegisteredEvents(userId: String): StateFlow<List<Event>> {
        val registeredIds = registrations[userId] ?: emptySet()
        val registeredList = allEvents.value.filter { it.id in registeredIds }
        return MutableStateFlow(registeredList).asStateFlow()
    }

    override suspend fun registerForEvent(userId: String, eventId: String): Result<Unit> = runCatching {
        val userRegs = registrations.getOrPut(userId) { mutableSetOf() }
        userRegs.add(eventId)
    }

    override suspend fun unregisterFromEvent(userId: String, eventId: String): Result<Unit> = runCatching {
        val userRegs = registrations[userId]
        userRegs?.remove(eventId)
    }

    override fun isUserRegistered(userId: String, eventId: String): Boolean {
        return registrations[userId]?.contains(eventId) == true
    }
}
