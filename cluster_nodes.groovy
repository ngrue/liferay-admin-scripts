import com.liferay.portal.kernel.cluster.*;

println "Current node is Master: "+ClusterMasterExecutorUtil.isMaster()

def localNode = ClusterExecutorUtil.getLocalClusterNode();
def allNodes = ClusterExecutorUtil.getClusterNodes()
for (node in allNodes) {
  if (node.equals(localNode)) {
    print "[ME] "
  }
  print node.getBindInetAddress()
  print "("
  print node.getClusterNodeId()
  println ")"
}
