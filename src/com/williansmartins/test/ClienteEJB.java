package com.williansmartins.test;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.exemplo.vendas.negocio.interfaces.UsuarioInterface;
import br.com.exemplo.vendas.negocio.model.vo.UsuarioVO;
import br.com.exemplo.vendas.util.dto.ServiceDTO;
import br.com.exemplo.vendas.util.exception.LayerException;

public class ClienteEJB
{

    Context context;
    Properties properties = new Properties();
    UsuarioInterface remoteUsuario;
    ServiceDTO requestDTO = new ServiceDTO();
    ServiceDTO responseDTO = new ServiceDTO();
    
    { 
    
        try
        {
            properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory"); 
            properties.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces"); 
            properties.put("java.naming.provider.url","localhost:1099"); 
            Context ctx = new InitialContext( properties );
            remoteUsuario = (UsuarioInterface) ctx.lookup( "UsuarioBean/remote" );
    	
        } catch ( NamingException e )
    	{
    	    System.out.println("Erro ao fazer lookup");
    	    e.printStackTrace();
	}

    }

    public static void main( String[] args ) throws RemoteException, LayerException
    {
	new ClienteEJB().inserir();
	new ClienteEJB().listar();
    }
    
    public void inserir() throws RemoteException, LayerException
    {
	UsuarioVO vo = new UsuarioVO( "will234"+new Date(), "senha1111", "grupo1111","perfil1111", true, new Date() );
	requestDTO.set( "usuarioVO", vo );
	responseDTO = remoteUsuario.inserirUsuario( requestDTO );
	Boolean sucesso = (Boolean) responseDTO.get( "resposta" );
	System.out.println("Inseriu?" + sucesso);
    }
    
    public void listar()
    {
	try
	{
	    responseDTO = remoteUsuario.selecionarTodosUsuarios( requestDTO );
	} catch ( RemoteException | LayerException e )
	{
	    System.out.println("Exceção: " + e.getMessage());
	    e.printStackTrace();
	}
	UsuarioVO[] lista = (UsuarioVO[]) responseDTO.get( "listaUsuario" );
	if ( lista != null )
	{
	    for ( int i = 0; i < lista.length; i++ )
	    {
		UsuarioVO usuarioVO = (UsuarioVO) lista[i];
		System.out.println( usuarioVO );
	    }
	}else{
	    System.out.println("Não veio nada na lista");
	}
    }
}
