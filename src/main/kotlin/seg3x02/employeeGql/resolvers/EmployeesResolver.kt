package seg3x02.employeeGql.resolvers

import org.springframework.stereotype.Controller
import org.springframework.data.mongodb.core.MongoOperations

import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import java.util.*


@Controller
class EmployeesResolver(private val employeeRepository: EmployeesRepository,
private val mongoOperations: MongoOperations) {


    @QueryMapping
    fun employees(): List<Employee> {
        return employeeRepository.findAll()
    }


    @QueryMapping
    fun employeeById(@Argument employeeId: String): Employee? {
        val employee = employeeRepository.findById(employeeId)
        return employee.orElse(null)
    }


    @MutationMapping
    fun newEmployee(@Argument("createEmployeeInput") input: CreateEmployeeInput) : Employee? {

            val employee =
                input.salary?.let { input.name?.let { it1 -> input.dateOfBirth?.let { it2 ->
                    input.city?.let { it3 ->
                        Employee(it1,
                            it2, it3, it, input.gender, input.email)
                    }
                } } }
        if (employee != null) {
            employee.id = UUID.randomUUID().toString()
        }
        if (employee != null) {
            employeeRepository.save(employee)
        }
            return employee
    }

    @MutationMapping
    fun deleteEmployee(@Argument("employeeId") id: String) : Boolean {
        employeeRepository.deleteById(id)
        return true
    }

    @MutationMapping
    fun updateEmployee(@Argument employeeId: String, @Argument("createEmployeeInput") input: CreateEmployeeInput) : Employee {
        val employee = employeeRepository.findById(employeeId)
        employee.ifPresent {
            if (input.name != null) {
                it.name = input.name
            }
            if (input.dateOfBirth != null) {
                it.dateOfBirth = input.dateOfBirth
            }
            if (input.city != null) {
                it.city = input.city
            }
            if (input.salary != null) {
                it.salary = input.salary
            }
            if (input.gender != null) {
                it.gender = input.gender
            }
            if (input.email != null) {
                it.email = input.email
            }
            employeeRepository.save(it)
        }
        return employee.get()
    }
}
 