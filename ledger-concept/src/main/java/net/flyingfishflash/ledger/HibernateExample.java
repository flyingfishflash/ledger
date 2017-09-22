package net.flyingfishflash.ledger;

public class HibernateExample
{
   private org.hibernate.SessionFactory _sessionFactory;
 
   final static public void main( String arguments[] )
   {
      HibernateExample example;
      TreeNode         roots[];
 
      example = new HibernateExample();
 
      example.makeTree();
      example.makeTree();
      System.out.println( "**********************" );
      System.out.println( "In-session display" );
      System.out.println( "**********************" );
      example.showTree();
      System.out.println( "**********************" );
      System.out.println( "Out-of-session display" );
      System.out.println( "**********************" );
      roots = example.getRoots();
      for ( TreeNode root : roots )
      {
         // This works outside of any session because eager loading is
         // specified on TreeNode._children
         System.out.println( "Display of root:" );
         root.display( "   " );
      }
 
      example.cleanup();
   }
 
   private HibernateExample()
   {
      org.hibernate.cfg.Configuration configuration;
 
      java.util.logging.Logger.getLogger( "org.hibernate" ).setLevel( java.util.logging.Level.SEVERE );  // Supress Hibernate's excessive output
 
      configuration = new org.hibernate.cfg.Configuration();
      configuration.setProperty( "hibernate.dialect",                 "org.hibernate.dialect.PostgreSQLDialect" );                                          // Customize this for your particular RDBMS
      configuration.setProperty( "hibernate.connection.driver_class", "org.postgresql.Driver" );                                                            // Customize this for your particular RDBMS
      configuration.setProperty( "hibernate.connection.url",          "jdbc:postgresql://localhost:5432/ledger-concept" );  // Customize this for your particular RDBMS
      configuration.setProperty( "hibernate.connection.username",     "gregory" );                                                                       // Customize this for your particular RDBMS
      configuration.setProperty( "hibernate.connection.password",     "" );     // Customize this for your particular RDBMS installation
      configuration.setProperty( "hibernate.connection.pool_size",    "1" );    // Customize this for your particular RDBMS installation
      configuration.setProperty( "hibernate.cache.provider_class",    "org.hibernate.cache.internal.NoCacheProvider" );  // This is not ready for prime-time
      configuration.setProperty( "hibernate.show_sql",                "false" );  // Tell hibernate to not echo the SQL
      configuration.setProperty( "hibernate.hbm2ddl.auto",            "create-drop" );
      configuration.addAnnotatedClass( TreeNode.class );
 
      _sessionFactory = configuration.buildSessionFactory();
   }
 
   final private void makeTree()
   {
      org.hibernate.Session     session;
      boolean                   committed;
      org.hibernate.Transaction transaction;
      TreeNode                  root;
      TreeNode                  child1;
      TreeNode                  child2;
      TreeNode                  grandchild11;
      TreeNode                  grandchild12;
      TreeNode                  grandchild13;
      TreeNode                  grandchild21;
      TreeNode                  grandchild22;
      TreeNode                  greatGrandchild121;
 
      session = _sessionFactory.openSession();
 
      try
      {
         committed = false;
         transaction = session.beginTransaction();
 
         try
         {
            root = TreeNode.createRoot( "MyRootData" );
            child1 = root.addChild( "MyChild1Data ");
            grandchild11 = child1.addChild( "MyGrandchild11Data" );
            grandchild12 = child1.addChild( "MyGrandchild12Data" );
            child2 = root.addChild( "MyChild2Data" );
            grandchild21 = child2.addChild( "MyGrandchild21Data" );
            grandchild22 = child2.addChild( "MyGrandchild22Data" );
            grandchild13 = child1.addChild( "MyGrandchild13Data" );
            greatGrandchild121 = grandchild12.addChild( "MyGreatGrandchild121Data" );
            session.save( root );  // This saves the entire structure because cascade saving
                                   // is employed.
            transaction.commit();
            committed = true;
         }
         finally
         {
            if ( !committed )
            {
               transaction.rollback();
            }
         }
      }
      finally
      {
         session.close();
      }
 
      return;
   }
 
   final private void showTree()
   {
      org.hibernate.Session     session;
      boolean                   committed;
      org.hibernate.Transaction transaction;
      java.util.List<TreeNode>  roots;
 
      session = _sessionFactory.openSession();
 
      try
      {
         committed = false;
         transaction = session.beginTransaction();
 
         try
         {
            roots = (java.util.List<TreeNode>)( session.createQuery("from TreeNode where _parent is null").list() );
 
            for ( TreeNode root : roots )
            {
               System.out.println( "Display of root:" );
               root.display( "   " );
            }
 
            transaction.commit();
            committed = true;
         }
         finally
         {
            if ( !committed )
            {
               transaction.rollback();
            }
         }
      }
      finally
      {
         session.close();
      }
 
      return;
   }
 
   final private TreeNode[] getRoots()
   {
      org.hibernate.Session     session;
      boolean                   committed;
      org.hibernate.Transaction transaction;
      java.util.List<TreeNode>  roots;
      TreeNode                  result[];
      int                       index;
 
      session = _sessionFactory.openSession();
 
      try
      {
         committed = false;
         transaction = session.beginTransaction();
 
         try
         {
            roots = (java.util.List<TreeNode>)( session.createQuery("from TreeNode where _parent is null").list() );
            result = new TreeNode[ roots.size() ];
            index = 0;
            for ( TreeNode root : roots )
            {
               result[ index ] = root;
               index++;
            }
            transaction.commit();
            committed = true;
         }
         finally
         {
            if ( !committed )
            {
               transaction.rollback();
            }
         }
      }
      finally
      {
         session.close();
      }
 
      return result;
   }
 
   final private void cleanup()
   {
      if ( _sessionFactory != null )
      {
         _sessionFactory.close();
      }
   }
}