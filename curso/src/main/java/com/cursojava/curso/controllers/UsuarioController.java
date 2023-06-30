package com.cursojava.curso.controllers;

import com.cursojava.curso.dao.UsuarioDao;
import com.cursojava.curso.models.Usuario;
import com.cursojava.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "usuario/{id}")
    public Usuario getUsuario(@PathVariable long id){

        Usuario usuario = new Usuario();

        usuario.setId(id);
        usuario.setNombre("Julian");
        usuario.setApellido("Zapata");
        usuario.setEmail("Zapataparrajulian@gmail.com");
        usuario.setTelefono("3195387870");
        usuario.setPassword("contraseña");
        return usuario;
    }

    @RequestMapping(value = "updateUsuario")
    public Usuario updateUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNombre("Julian");
        usuario.setApellido("Zapata");
        usuario.setEmail("Zapataparrajulian@gmail.com");
        usuario.setTelefono("3195387870");
        return usuario;
    }

    @RequestMapping(value = "api/deleteUsuario/{id}", method = RequestMethod.DELETE)
    public void deleteUsuario(@RequestHeader(value = "Authorization") String token, @PathVariable long id){

        if(!validarToken(token)){
            return;
        }

        usuarioDao.eliminar(id);
    }

    @RequestMapping(value = "usuarios")
    public List<Usuario> getUsuarios(@RequestHeader(value = "Authorization") String token){

        if(!validarToken(token)){
            return null;
        }

        return usuarioDao.getUsuarios();
    }

    private boolean validarToken(String token){
        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }
    @RequestMapping(value = "api/registrarUsuario", method = RequestMethod.POST)
    public void registrarUsuarios(@RequestBody Usuario usuario){

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());

        usuario.setPassword(hash);

        usuarioDao.registrar(usuario);
    }
}
