package org.example.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.example.models.dto.UserCreateDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    private var username: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @JsonIgnore // Don't expose password in JSON responses
    @Column(nullable = false)
    private var password: String = "",

    @Column(nullable = false)
    var role: String = Roles.USER.roleName
) : UserDetails {

    fun setPassword(password: String){
        this.password = password
    }

    fun setUsername(username: String){
        this.username = username
    }

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_$role"))

    override fun getUsername(): String = username

    @JsonIgnore
    override fun getPassword(): String = password

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @JsonIgnore
    override fun isEnabled(): Boolean = true

    companion object {
        @JvmStatic
        fun fromDto(userDto: UserCreateDto): AppUser{
            return AppUser(null,userDto.getUsername(), userDto.getEmail(),userDto.getPassword(), Roles.USER.roleName)
        }
    }
}
