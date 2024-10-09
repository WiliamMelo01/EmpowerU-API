package org.wiliammelo.empoweru.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity class representing a Student.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Student extends User {


}
