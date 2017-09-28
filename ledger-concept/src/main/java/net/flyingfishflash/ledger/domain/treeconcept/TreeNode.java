package net.flyingfishflash.ledger.domain.treeconcept;


//https://coderanch.com/t/629306/Simple-Hibernate-Lazy-Loading
	
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TreeNode
{
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   //@GeneratedValue( generator = "increment" )
   //@GenericGenerator( name = "increment", strategy = "increment" )
   private long                    _id;
   @javax.persistence.ManyToOne
   private TreeNode                _parent;
   @javax.persistence.OneToMany( mappedBy = "_parent", cascade = javax.persistence.CascadeType.ALL, fetch = javax.persistence.FetchType.EAGER )
   private java.util.Set<TreeNode> _children = new java.util.HashSet<TreeNode>();
   private String                  _data;
 
   private TreeNode()
   {
   }
 
   private TreeNode( String data )
   {
      _parent = null;
      _data = data;
   }
 
   private TreeNode( final TreeNode parent,
                           String   data )
   {
      if ( parent == null )
      {
         throw new IllegalArgumentException( "parent required" );
      }
 
      _parent = parent;
      _data = data;
      registerInParentsChilds();
   }
 
   public TreeNode addChild( String data )
   {
      return new TreeNode( this,
                           data );
   }
 
   public void display( String margin )
   {
      System.out.println( margin + _data );
 
      for ( TreeNode child : _children )
      {
         child.display( margin + "   " );
      }
   }
 
   private void registerInParentsChilds()
   {
      _parent._children.add( this );
   }
 
   //public java.util.List<TreeNode> getChildren()
   //{
   //    return java.util.Collections.unmodifiableList( _children );
   //}
 
   public String getData()
   {
      return _data;
   }
 
   public static TreeNode createRoot( String data )
   {
      return new TreeNode( data );
   }
}