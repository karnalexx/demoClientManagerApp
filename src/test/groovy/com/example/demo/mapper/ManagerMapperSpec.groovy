package com.example.demo.mapper

import com.example.demo.DemoApplication
import com.example.demo.domain.Manager
import com.example.demo.mapper.ManagerMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ContextConfiguration(classes = DemoApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@ComponentScan("com.example.demo")
class ManagerMapperSpec extends Specification {

    @Autowired
    private ManagerMapper managerMapper

    def "should find manager by id"() {
        expect:
        Manager manager = managerMapper.findById(1)
        manager != null
    }

    def "should find all managers"() {
        expect:
        List<Manager> managers = managerMapper.findAll()
        !managers.isEmpty()
        managers.size() == managerMapper.count()
    }

    def "should create new manager"() {
        expect:
        int origCount = managerMapper.count()

        Manager alternate = managerMapper.findById(1)

        managerMapper.save(new Manager(firstName: "fNameNew", middleName: "mNameNew", lastName: "lNameNew",
                phone: "123-123-123", alternate: alternate))

        managerMapper.count() == origCount + 1
    }

    def "should update existing manager data"() {
        given:

        Manager origManager = managerMapper.findById(2)
        Manager newAlternate = managerMapper.findById(3)

        expect:

        origManager.with {
            alternate.id != newAlternate.id
            firstName == "fName2"
            middleName == "mName2"
            lastName == "lName2"
            phone == "222-222-222"
        }

        origManager.with {
            firstName = "fNameUpd"
            middleName = "mNameUpd"
            lastName = "lNameUpd"
            phone = "111-222-333"
            alternate = newAlternate
        }

        managerMapper.update(origManager)

        def managerUpdated = managerMapper.findById(origManager.id)
        managerUpdated.with {
            alternate.id == newAlternate.id
            firstName == "fNameUpd"
            middleName == "mNameUpd"
            lastName == "lNameUpd"
            phone == "111-222-333"
        }
    }

    def "should delete manager with id"() {
        given:
        int origCount = managerMapper.count()

        expect:
        managerMapper.delete(5)
        managerMapper.count() == origCount - 1
    }

}