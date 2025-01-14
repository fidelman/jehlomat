package main

import TestUtils.Companion.loginUser
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Organization
import model.OrganizationRegistration
import model.user.User
import org.junit.Test
import org.mindrot.jbcrypt.BCrypt
import services.DatabaseService
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

const val ORGANIZATION_API_PATH = "/api/v1/jehlomat/organization"

val ORGANIZATION = Organization(
    1,
    name="ceska jehlova",
    true
)


class OrganizationTest {

    var database: DatabaseService = DatabaseService()

    @BeforeTest
    fun beforeEach() {
        database.cleanUsers()
        database.cleanTeams()
        database.cleanOrganizations()
    }

    @Test
    fun testGetOrganization() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)
        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/$orgId") {
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                """{
  "id" : """ + orgId + """,
  "name" : "ceska jehlova",
  "verified" : true
}""",
                response.content
            )
        }
    }

    @Test
    fun testGetAllOrganizationsNotEmpty() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)
        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH") {
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                """[ {
  "id" : """.trimIndent() + orgId + """,
  "name" : "ceska jehlova",
  "verified" : true
} ]""",
                response.content)
        }
    }

    @Test
    fun testGetOrganizationNotFound() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)
        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/administrator@example.org"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.NotFound, response.status())
            assertEquals("Organization not found", response.content)
        }
    }

    @Test
    fun testGetUsersOk() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        val differentOrgId = database.insertOrganization(ORGANIZATION.copy(name = "differentOrg"))

        val userId1 = database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val userId2 = database.insertUser(USER.copy(username = "Tomas Novak", email = "email2", verified = true, organizationId = orgId, teamId = null))
        database.insertUser(USER.copy(email = "email3",verified = true, organizationId = differentOrgId, teamId = null))
        val token = loginUser(USER.email, USER.password)

        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/$orgId/users"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.OK, response.status())

            assertEquals(
                """[ {
  "id" : """ + userId1 + """,
  "username" : "Franta Pepa 1",
  "organizationId" : """ + orgId + """,
  "teamId" : null,
  "isAdmin" : false
}, {
  "id" : """ + userId2 + """,
  "username" : "Tomas Novak",
  "organizationId" : """ + orgId + """,
  "teamId" : null,
  "isAdmin" : false
} ]""",
                response.content)
        }
    }

    @Test
    fun testGetUsersOrgNotFound() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)

        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/differentOrg/users"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.NotFound, response.status())
            assertEquals("Organization not found", response.content)
        }
    }

    @Test
    fun testGetUsersNotAllowed() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)

        val differentOrgId = database.insertOrganization(ORGANIZATION.copy(name = "differentOrg"))

        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/${differentOrgId}/users"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.Forbidden, response.status())
        }
    }


    @Test
    fun testGetTeamsOk() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)

        val teamId1 = database.insertTeam(TEAM.copy(name = "team 1", organizationId = orgId))
        val teamId2 = database.insertTeam(TEAM.copy(name = "team 2", organizationId = orgId))

        val differentOrgId = database.insertOrganization(ORGANIZATION.copy(name = "different org"))
        database.insertTeam(TEAM.copy(name = "team 3", organizationId = differentOrgId))

        val team1 = database.selectTeamById(teamId1)!!

        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/$orgId/teams"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.OK, response.status())

            assertEquals(
                """[ {
  "id" : """ + teamId1 + """,
  "name" : "team 1",
  "location" : {
    "id" : """ + team1.location.id + """,
    "okres" : """" + LOCATION.okres + """",
    "okresName" : """" + LOCATION.okresName + """",
    "obec" : """ + LOCATION.obec + """,
    "obecName" : """" + LOCATION.obecName + """",
    "mestkaCast" : """ + LOCATION.mestkaCast + """,
    "mestkaCastName" : """" + LOCATION.mestkaCastName + """"
  },
  "organizationId" : """ + orgId + """
}, {
  "id" : """ + teamId2 + """,
  "name" : "team 2",
  "location" : {
    "id" : """ + team1.location.id + """,
    "okres" : """" + LOCATION.okres + """",
    "okresName" : """" + LOCATION.okresName + """",
    "obec" : """ + LOCATION.obec + """,
    "obecName" : """" + LOCATION.obecName + """",
    "mestkaCast" : """ + LOCATION.mestkaCast + """,
    "mestkaCastName" : """" + LOCATION.mestkaCastName + """"
  },
  "organizationId" : """ + orgId + """
} ]""",
                response.content)
        }
    }

    @Test
    fun testGetTeamsOrgNotFound() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)

        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/differentOrg/teams"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.NotFound, response.status())
            assertEquals("Organization not found", response.content)
        }
    }

    @Test
    fun testGetTeamsNotAllowed() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)

        val differentOrgId = database.insertOrganization(ORGANIZATION.copy(name = "differentOrg"))

        with(handleRequest(HttpMethod.Get, "$ORGANIZATION_API_PATH/${differentOrgId}/teams"){
            addHeader("Authorization", "Bearer $token")
        }) {
            assertEquals(HttpStatusCode.Forbidden, response.status())
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPostOrganization(): Unit = withTestApplication({ module(testing = true) }) {
        val registration = OrganizationRegistration("orgName", "email@email.cz", "aaBB11aa")
        with(handleRequest(HttpMethod.Post, "$ORGANIZATION_API_PATH") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(registration))
        }) {
            assertEquals(HttpStatusCode.Created, response.status())

            val createdOrganization = database.selectOrganizationByName(registration.name)
            assertNotNull(createdOrganization)
            assertEquals(registration.name, createdOrganization.name)
            assertEquals(false, createdOrganization.verified)

            val createdUser = database.selectUserByEmail(registration.email)
            assertNotNull(createdUser)
            assertEquals(registration.email, createdUser.email)
            assertEquals(createdOrganization.id, createdUser.organizationId)
            assertEquals(null, createdUser.teamId)
            assertEquals(true, createdUser.isAdmin)
            assert(BCrypt.checkpw("aaBB11aa", createdUser.password))
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPostAlreadyExistingOrganization() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Post, "$ORGANIZATION_API_PATH") {
            database.insertOrganization(ORGANIZATION)
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(OrganizationRegistration(ORGANIZATION.name, "email@email.cz", "aaAA11aa")))
        }) {
            assertEquals(HttpStatusCode.Conflict, response.status())
            assertEquals("Organization name already exists", response.content)
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPostAlreadyExistingEmail() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Post, "$ORGANIZATION_API_PATH") {
            val orgId = database.insertOrganization(ORGANIZATION)
            database.insertUser(User(0, "email@email.cz", "orgName", "aaAA11aa",false, "", orgId, null, false))
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(OrganizationRegistration("new org", "email@email.cz", "aaAA11aa")))
        }) {
            assertEquals(HttpStatusCode.Conflict, response.status())
            assertEquals("E-mail already taken", response.content)
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPostWrongEmail() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Post, "$ORGANIZATION_API_PATH") {
            database.insertOrganization(ORGANIZATION)
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(OrganizationRegistration("new org", "email", "aaAA11aa")))
        }) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            assertEquals("Wrong e-mail format", response.content)
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPostWrongName() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Post, "$ORGANIZATION_API_PATH") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(OrganizationRegistration("", "email", "aaAA11aa")))
        }) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            assertEquals("Wrong e-mail format", response.content)
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPostWrongPassword() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Post, "$ORGANIZATION_API_PATH") {
            database.insertOrganization(ORGANIZATION)
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(OrganizationRegistration("new org", "email@email.cz", "aa")))
        }) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
            assertEquals("Wrong password format", response.content)
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPutOrganizationNotExists() = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null, isAdmin = true))
        val token = loginUser(USER.email, USER.password)

        with(handleRequest(HttpMethod.Put, "$ORGANIZATION_API_PATH/") {
            addHeader("Authorization", "Bearer $token")
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(ORGANIZATION))
        }) {
            assertEquals(HttpStatusCode.NotFound, response.status())
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPutOrganizationOk(): Unit = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null, isAdmin = true))
        val token = loginUser(USER.email, USER.password)
        val newOrganization = ORGANIZATION.copy(name="different name")

        with(handleRequest(HttpMethod.Put, "$ORGANIZATION_API_PATH") {
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Bearer $token")
            setBody(Json.encodeToString(newOrganization.copy(id = orgId)))
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNull(database.selectOrganizationByName(ORGANIZATION.name))
            assertNotNull(database.selectOrganizationByName(newOrganization.name))
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun testPutOrganizationForbidden(): Unit = withTestApplication(Application::module) {
        val orgId = database.insertOrganization(ORGANIZATION)
        database.insertUser(USER.copy(verified = true, organizationId = orgId, teamId = null))
        val token = loginUser(USER.email, USER.password)
        val newOrganization = ORGANIZATION.copy(name="different name")

        with(handleRequest(HttpMethod.Put, "$ORGANIZATION_API_PATH") {
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Bearer $token")
            setBody(Json.encodeToString(newOrganization.copy(id = orgId)))
        }) {
            assertEquals(HttpStatusCode.Forbidden, response.status())
        }
    }
}

