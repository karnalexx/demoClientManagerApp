package com.example.demo.mapper

import com.example.demo.DemoApplication
import com.example.demo.domain.Client
import com.example.demo.domain.Manager
import com.example.demo.mapper.ClientMapper
import com.example.demo.mapper.ManagerMapper
import org.junit.Test
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ContextConfiguration(classes = DemoApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@ComponentScan("com.example.demo")
class ClientMapperSpec extends Specification {

    @Autowired
    private ClientMapper clientMapper

    @Autowired
    private ManagerMapper managerMapper

    def "should find client by id"() {
        expect:
        Client client = clientMapper.findById(1)
        client != null
    }

    def "should find all clients with deleted = FALSE"() {
        expect:
        List<Client> clients = clientMapper.findAll()
        !clients.isEmpty()
        clients.size() == 3
    }

    def "should find client list for manager provided"() {
        expect:
        List<Client> clients = clientMapper.findClientsForManager(2)
        clients.size() == 1

        clients[0].id == 2
        clients[0].name == "client2"
        clients[0].manager.id == 2
    }

    def "should create new client"() {
        expect:
        int origCount = clientMapper.count()

        Manager manager = managerMapper.findById(2)

        clientMapper.save(new Client(name: "clientNew", address: "addressNew", manager: manager))

        clientMapper.count() == origCount + 1
    }

    def "should update existing client data"() {
        given:

        Client origClient = clientMapper.findById(2)

        expect:

        origClient.with {
            manager.id == 2
            name == "client2"
            address == "address2"
        }

        origClient.with {
            manager.id = 3
            name = "clientUpd"
            address = "addressUpd"
        }

        clientMapper.update(origClient)

        def clientUpdated = clientMapper.findById(origClient.id)
        clientUpdated.with {
            manager.id == 3
            name == "clientUpd"
            address == "addressUpd"
        }
    }

    def "should mark client as deleted"() {
        given:
        Client origClient = clientMapper.findById(1)
        !origClient.deleted

        expect:
        clientMapper.delete(1)

        def clientDeleted = clientMapper.findById(origClient.id)
        clientDeleted.deleted
    }

}
