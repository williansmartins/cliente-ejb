package com.williansmartins.test;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.junit.Test;

import br.com.exemplo.vendas.negocio.interfaces.UsuarioInterface;
import br.com.exemplo.vendas.negocio.model.vo.UsuarioVO;
import br.com.exemplo.vendas.util.dto.ServiceDTO;
import br.com.exemplo.vendas.util.exception.LayerException;

public class UsuarioTest
{
    Context context;
    Properties properties = new Properties();
    UsuarioInterface remoteUsuario;
    ServiceDTO requestDTO = new ServiceDTO();
    ServiceDTO responseDTO = new ServiceDTO();
    Boolean sucesso1;
    Boolean sucesso2;

    {

	try
	{
	    properties.put( "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" );
	    properties.put( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );
	    properties.put( "java.naming.provider.url", "localhost:1099" );
	    Context ctx = new InitialContext( properties );
	    remoteUsuario = (UsuarioInterface) ctx.lookup( "UsuarioBean/remote" );

	} catch ( NamingException e )
	{
	    System.out.println( "Erro ao fazer lookup" );
	    e.printStackTrace();
	}

    }

    @Test
    public void inserirLocalizarExcluirUsuario( )
    {
	UsuarioVO vo = new UsuarioVO( "willians-inserir" + new Date(), "senha", "grupo", "perfil", true, new Date() );
	requestDTO.set( "usuarioVO", vo );
	requestDTO.set( "loginUsuario", vo.getLogin() );
	try
	{
	    //Testa o inserir
	    responseDTO = remoteUsuario.inserirUsuario( requestDTO );
	    Boolean sucesso1 = (Boolean) responseDTO.get( "resposta" );
	    Assert.assertTrue( sucesso1 );
	    
	    //Testa o localizar
	    responseDTO = remoteUsuario.localizarPorLogin( requestDTO, vo.getLogin() );
	    vo = (UsuarioVO) responseDTO.get( "getUsuario" );
	    Assert.assertTrue( vo != null );
	    
	    //Testa o excluir
	    Boolean sucesso2 = (Boolean) remoteUsuario.excluirUsuarioPorLogin( requestDTO ).get( "resposta" );
	    Assert.assertTrue( sucesso2 );
	    
	    
	} catch ( RemoteException | LayerException e )
	{
	    e.printStackTrace();
	}
    }
    
    @Test
    public void inserirElistarTodosUsuariosInseridos( )
    {
	UsuarioVO vo1 = new UsuarioVO( "willians-1" + new Date(), "senha", "grupo", "perfil", true, new Date() );
	UsuarioVO vo2 = new UsuarioVO( "willians-2" + new Date(), "senha", "grupo", "perfil", true, new Date() );
	UsuarioVO vo3 = new UsuarioVO( "willians-3" + new Date(), "senha", "grupo", "perfil", true, new Date() );
	
	
	try
	{
	    //Testamos o inserir
	    requestDTO.set( "usuarioVO", vo1 );
	    requestDTO.set( "loginUsuario", vo1.getLogin() );
	    responseDTO = remoteUsuario.inserirUsuario( requestDTO );
	    sucesso1 = (Boolean) responseDTO.get( "resposta" );
	    Assert.assertTrue( sucesso1 );
	    
	    requestDTO.set( "usuarioVO", vo2 );
	    requestDTO.set( "loginUsuario", vo2.getLogin() );
	    responseDTO = remoteUsuario.inserirUsuario( requestDTO );
	    sucesso1 = (Boolean) responseDTO.get( "resposta" );
	    Assert.assertTrue( sucesso1 );
	    
	    requestDTO.set( "usuarioVO", vo3 );
	    requestDTO.set( "loginUsuario", vo3.getLogin() );
	    responseDTO = remoteUsuario.inserirUsuario( requestDTO );
	    sucesso1 = (Boolean) responseDTO.get( "resposta" );
	    Assert.assertTrue( sucesso1 );
	    
	    //Testa o listar
	    responseDTO = remoteUsuario.selecionarTodosUsuarios( requestDTO );
	    UsuarioVO[] usuarios = (UsuarioVO[]) responseDTO.get( "listaUsuario" );
	    Assert.assertTrue( usuarios.length >= 3 );
	    Assert.assertTrue( usuarios[0].getLogin() != null );
	    
	    //Testa o excluir
	    requestDTO.set( "usuarioVO", vo1 );
	    requestDTO.set( "loginUsuario", vo1.getLogin() );
	    sucesso2 = (Boolean) remoteUsuario.excluirUsuarioPorLogin( requestDTO ).get( "resposta" );
	    Assert.assertTrue( sucesso2 );

	    requestDTO.set( "usuarioVO", vo2 );
	    requestDTO.set( "loginUsuario", vo2.getLogin() );
	    sucesso2 = (Boolean) remoteUsuario.excluirUsuarioPorLogin( requestDTO ).get( "resposta" );
	    Assert.assertTrue( sucesso2 );
	    
	    requestDTO.set( "usuarioVO", vo3 );
	    requestDTO.set( "loginUsuario", vo3.getLogin() );
	    sucesso2 = (Boolean) remoteUsuario.excluirUsuarioPorLogin( requestDTO ).get( "resposta" );
	    Assert.assertTrue( sucesso2 );
	    
	} catch ( RemoteException | LayerException e )
	{
	    e.printStackTrace();
	}
	
    }

}
