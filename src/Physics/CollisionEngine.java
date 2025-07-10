package Physics;

public class CollisionEngine {
	
	public CollisionEngine() {
		Vector3 A1 = new Vector3(0, 0, 0);
		Vector3 A2 = new Vector3(10, 0, 0);
		Vector3 Ad = Vector3.subtract(A1, A2).getNormalized();
		
		Vector3 P0 = new Vector3(2, 1, 1);
		System.out.println("Distance: " + shortestDistance(A1, Ad, P0));
	}
	
	public double shortestDistance(Vector3 V0, Vector3 Vd, Vector3 P) {
		double numer = Vector3.dotProduct(P, Vd) - Vector3.dotProduct(V0, Vd);
		double denom = Vector3.dotProduct(Vd, Vd);
		double t = numer / denom;
		
		Vector3 P1 = new Vector3(V0.x + t * Vd.x, V0.y + t * Vd.y, V0.z + t * Vd.z);
		double distance = Vector3.subtract(P1, P).getMagnitude();
		return distance;
	}
	
	public Vector3 OrientedBoxToOrientedBoxCollision(BoxCollider box1, BoxCollider box2) {
		Vector3 displacement = new Vector3(0, 0, 0);
		
		//Edge to edge test
		int[] lines = {0, 1, 1, 2, 2, 3, 3, 0, 
					   4, 5, 5, 6, 6, 7, 7, 4,
					   4, 0, 1, 5, 2, 6, 3, 7};
		
		for(int i = 0; i < 12; i++) {
			//Top plane and right plane
			Vector3 displacement1 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[0], box2.prev[1], 
															 box2.prev[0], box2.prev[1], box2.prev[3], 
															 box2.prev[4], box2.prev[5], box2.prev[0]);
			if(displacement1 != null && displacement1.getMagnitude() > displacement.getMagnitude()) displacement = displacement1;
			
			//Top plane and left plane
			Vector3 displacement2 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[2], box2.prev[3], 
															 box2.prev[0], box2.prev[1], box2.prev[3], 
															 box2.prev[3], box2.prev[2], box2.prev[7]);
			if(displacement2 != null && displacement2.getMagnitude() > displacement.getMagnitude()) displacement = displacement2;
			
			//Top plane and front plane
			Vector3 displacement3 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[3], box2.prev[0], 
															 box2.prev[0], box2.prev[1], box2.prev[3], 
															 box2.prev[0], box2.prev[3], box2.prev[4]);
			if(displacement3 != null && displacement3.getMagnitude() > displacement.getMagnitude()) displacement = displacement3;
			
			//Top plane and back plane
			Vector3 displacement4 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[3], box2.prev[0], 
															 box2.prev[0], box2.prev[1], box2.prev[3], 
															 box2.prev[0], box2.prev[3], box2.prev[4]);
			if(displacement4 != null && displacement4.getMagnitude() > displacement.getMagnitude()) displacement = displacement4;
			
			//Bottom plane and right plane
			Vector3 displacement5 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[4], box2.prev[5], 
															 box2.prev[4], box2.prev[5], box2.prev[7], 
															 box2.prev[4], box2.prev[5], box2.prev[0]);
			if(displacement5 != null && displacement5.getMagnitude() > displacement.getMagnitude()) displacement = displacement5;
			
			//Bottom plane and left plane
			Vector3 displacement6 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[6], box2.prev[7], 
															 box2.prev[4], box2.prev[5], box2.prev[7], 
															 box2.prev[3], box2.prev[2], box2.prev[7]);
			if(displacement6 != null && displacement6.getMagnitude() > displacement.getMagnitude()) displacement = displacement6;
			
			//Bottom plane and front plane
			Vector3 displacement7 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[7], box2.prev[4], 
															 box2.prev[4], box2.prev[5], box2.prev[7], 
															 box2.prev[0], box2.prev[3], box2.prev[4]);
			if(displacement7 != null && displacement7.getMagnitude() > displacement.getMagnitude()) displacement = displacement7;
			
			//Bottom plane and back plane
			Vector3 displacement8 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[5], box2.prev[6], 
															 box2.prev[4], box2.prev[5], box2.prev[7], 
															 box2.prev[5], box2.prev[6], box2.prev[1]);
			if(displacement8 != null && displacement8.getMagnitude() > displacement.getMagnitude()) displacement = displacement8;
			
			//Front plane and right plane
			Vector3 displacement9 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[0], box2.prev[4], 
															 box2.prev[0], box2.prev[3], box2.prev[4], 
															 box2.prev[4], box2.prev[5], box2.prev[0]);
			if(displacement9 != null && displacement9.getMagnitude() > displacement.getMagnitude()) displacement = displacement9;
			
			//Front plane and left plane
			Vector3 displacement10 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[3], box2.prev[7], 
															 box2.prev[0], box2.prev[3], box2.prev[4], 
															 box2.prev[3], box2.prev[2], box2.prev[7]);
			if(displacement10 != null && displacement10.getMagnitude() > displacement.getMagnitude()) displacement = displacement10;
			
			//Back plane and right plane
			Vector3 displacement11 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[1], box2.prev[5], 
															 box2.prev[5], box2.prev[6], box2.prev[1], 
															 box2.prev[4], box2.prev[5], box2.prev[0]);
			if(displacement11 != null && displacement11.getMagnitude() > displacement.getMagnitude()) displacement = displacement11;
			
			//Back plane and right plane
			Vector3 displacement12 = edgeToPlaneEdgeCollision(box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															 box2.prev[2], box2.prev[6], 
															 box2.prev[5], box2.prev[6], box2.prev[1], 
															 box2.prev[3], box2.prev[2], box2.prev[7]);
			if(displacement12 != null && displacement12.getMagnitude() > displacement.getMagnitude()) displacement = displacement12;
		}
		/*
		if(displacement.getMagnitude() > 0) {
			return displacement;
		}
		*/
		//Corner to plane test
		for(int i = 0; i < 8; i++) {
			//Top face of cube
			Vector3 displacement1 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[0], box2.prev[1], box2.prev[3], false); //1 2 4
			if(displacement1 != null && displacement1.getMagnitude() > displacement.getMagnitude()) displacement = displacement1;
			
			//Bottom face of cube
			Vector3 displacement2 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[4], box2.prev[7], box2.prev[5], false); //5 8 6
			if(displacement2 != null && displacement2.getMagnitude() > displacement.getMagnitude()) displacement = displacement2;
			
			//Left face of cube
			Vector3 displacement3 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[3], box2.prev[2], box2.prev[7], false); //4 3 8
			if(displacement3 != null && displacement3.getMagnitude() > displacement.getMagnitude()) displacement = displacement3;
			
			//Right face of cube
			Vector3 displacement4 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[4], box2.prev[5], box2.prev[0], false); //5 6 1
			if(displacement4 != null && displacement4.getMagnitude() > displacement.getMagnitude()) displacement = displacement4;
			
			//Front face of cube
			Vector3 displacement5 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[4], box2.prev[0], box2.prev[7], false); //5 1 8
			if(displacement5 != null && displacement5.getMagnitude() > displacement.getMagnitude()) displacement = displacement5;
			
			//Back face of cube
			Vector3 displacement6 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[1], box2.prev[5], box2.prev[2], false); //2 6 3
			if(displacement6 != null && displacement6.getMagnitude() > displacement.getMagnitude()) displacement = displacement6;
		}
		
		if(displacement.getMagnitude() > 0) {
			return displacement;
		}
		
		//System.out.println("No collision found!");
		
		return null;
	}
	
	public Vector3 lineToPlaneCollision(Vector3 l0, Vector3 l1, Vector3 p0, Vector3 p1, Vector3 p3, boolean print) {
		Vector3 normal = Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(p3, p0)).getNormalized();
		if(print) System.out.println("Normal: [" + normal.x + ", " + normal.y + ", " + normal.z + "]");
		Vector3 Vd = Vector3.subtract(l1, l0).getNormalized();
		if(print) System.out.println("Vd: [" + Vd.x + ", " + Vd.y + ", " + Vd.z + "]");
		
		if(Vector3.dotProduct(Vd, normal) > 0) {
			return null;
		}
		
		double numerator = Vector3.dotProduct(p0, normal) - Vector3.dotProduct(l0, normal);
		double denominator = Vector3.dotProduct(Vd, normal);
		if(denominator == 0) {
			if(print) System.out.println("Zero denominator");
			return null;
		}
		double t = numerator / denominator;
		Vector3 intersect = new Vector3(l0.x + t * Vd.x, l0.y + t * Vd.y, l0.z + t * Vd.z);
		double xMin = Math.min(l0.x, l1.x); //l0.x;
		double yMin = Math.min(l0.y, l1.y); //l0.y;
		double zMin = Math.min(l0.z, l1.z); //l0.z
		double xMax = Math.max(l0.x, l1.x); //l1.x;
		double yMax = Math.max(l0.y, l1.y); //l1.y;
		double zMax = Math.max(l0.z, l1.z); //l1.z;
		/*
		if(l0.x > l1.x) {
			xMin = l1.x;
			xMax = l0.x;
		}
		if(l0.y > l1.y) {
			yMin = l1.y;
			yMax = l0.y;
		}
		if(l0.z > l1.z) {
			zMin = l1.z;
			zMax = l0.z;
		}
		*/
		if(intersect.x < xMin || intersect.x > xMax || intersect.y < yMin || intersect.y > yMax || intersect.z < zMin || intersect.z > zMax) {
			if(print) System.out.println("Not between segment endpoints");
			if(print) System.out.println("t: " + t);
			if(print) System.out.println("[" + xMin + ", " + yMin + ", " + zMin + "] [" + xMax + ", " + yMax + ", " + zMax + "] [" + intersect.x + ", " + intersect.y + ", " + intersect.z + "]");
			return null;
		}
		
		//System.out.println("Potential corner-plane collision found...");
		
		double area = Vector3.dotProduct(normal, Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(p3,  p0)));
		double c1 = Vector3.dotProduct(normal, Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(intersect, p0))) / area;
		double c2 = Vector3.dotProduct(normal, Vector3.crossProduct(Vector3.subtract(p0, p3), Vector3.subtract(intersect, p3))) / area;
		if(c1 < 0 || c1 > 1 || c2 < 0 || c2 > 1) {
			if(print) System.out.println("Not on plane");
			return null;
		}
		
		//System.out.println("Corner-plane intersection");
		
		numerator = Vector3.dotProduct(p0, normal) - Vector3.dotProduct(l1, normal);
		denominator = Vector3.dotProduct(normal, normal);
		t = numerator / denominator;
		
		Vector3 displacement = new Vector3(t * normal.x, t * normal.y, t * normal.z);
		
		//System.out.println("Displacement: [" + displacement.x + ", " + displacement.y + ", " + displacement.z + "]");
		
		return displacement;
	}
	
	public Vector3 edgeToPlaneEdgeCollision(Vector3 e0, Vector3 e1, Vector3 pe0, Vector3 pe1, Vector3 p10, Vector3 p11, Vector3 p13, Vector3 p20, Vector3 p21, Vector3 p23) {
		Vector3 planeIntersect1 = lineToPlaneIntersection(e0, e1, p10, p11, p13);
		Vector3 planeIntersect2 = lineToPlaneIntersection(e0, e1, p20, p21, p23);
		if(planeIntersect1 == null || planeIntersect2 == null) {
			return null;
		}
		//System.out.println("PlaneIntersect1: " + planeIntersect1);
		//System.out.println("EdgeToPlane Intersect1: " + planeIntersect1.x + " " + planeIntersect1.y + " " + planeIntersect1.z);
		//Finds point on edge between planes
		Vector3 Vd = Vector3.subtract(pe1, pe0).getNormalized();
		double numerator = Vector3.dotProduct(planeIntersect1, Vd) - Vector3.dotProduct(pe0, Vd);
		double denominator = Vector3.dotProduct(Vd, Vd);
		double t = numerator / denominator;
		
		Vector3 point1 = new Vector3(pe0.x + t * Vd.x, pe0.y + t * Vd.y, pe0.z + t * Vd.z);
		//Finds point on tested edge
		Vd = Vector3.subtract(e1, e0).getNormalized();
		numerator = Vector3.dotProduct(point1, Vd) - Vector3.dotProduct(e0, Vd);
		denominator = Vector3.dotProduct(Vd, Vd);
		t = numerator / denominator;
		
		Vector3 point2 = new Vector3(e0.x + t * Vd.x, e0.y + t * Vd.y, e0.z + t * Vd.z);
		
		return Vector3.subtract(point1, point2);
	}
	
	public Vector3 lineToPlaneIntersection(Vector3 l0, Vector3 l1, Vector3 p0, Vector3 p1, Vector3 p3) {
		Vector3 normal = Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(p3, p0)).getNormalized();
		Vector3 Vd = Vector3.subtract(l1, l0).getNormalized();
		double numerator = Vector3.dotProduct(p0, normal) - Vector3.dotProduct(l0, normal);
		double denominator = Vector3.dotProduct(Vd, normal);
		if(denominator == 0) {
			return null;
		}
		double t = numerator / denominator;
		Vector3 intersect = new Vector3(l0.x + t * Vd.x, l0.y + t * Vd.y, l0.z + t * Vd.z);
		double xMin = l0.x;
		double yMin = l0.y;
		double zMin = l0.z;
		double xMax = l1.x;
		double yMax = l1.y;
		double zMax = l1.z;
		if(l0.x > l1.x) {
			xMin = l1.x;
			xMax = l0.x;
		}
		if(l0.y > l1.y) {
			yMin = l1.y;
			yMax = l0.y;
		}
		if(l0.z > l1.z) {
			zMin = l1.z;
			zMax = l0.z;
		}
		if(intersect.x < xMin || intersect.x > xMax || intersect.y < yMin || intersect.y > yMax || intersect.z < zMin || intersect.z > zMax) {
			return null;
		}
		double area = Vector3.dotProduct(normal, Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(p3,  p0)));
		double c1 = Vector3.dotProduct(normal, Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(intersect, p0))) / area;
		double c2 = Vector3.dotProduct(normal, Vector3.crossProduct(Vector3.subtract(p0, p3), Vector3.subtract(intersect, p3))) / area;
		if(c1 < 0 || c1 > 1 || c2 < 0 || c2 > 1) {
			return null;
		}
		
		//System.out.println("Found corner-plane intersection!");
		
		return intersect;
	}
	
	public Vector3 edgeToPlaneEdgeCollision2(Vector3 ei0, Vector3 ei1, Vector3 ef0, Vector3 ef1, Vector3 pe0, Vector3 pe1, Plane plane1) {
		//Gets intersection point between previous edge position and constructed plane
		Vector3 vel = Vector3.subtract(ef0, ei0);
		
		Vector3 normal1 = Vector3.crossProduct(Vector3.subtract(pe1, pe0), vel).getNormalized();
		Vector3 Vd1 = Vector3.subtract(ei1, ei0).getNormalized();
		double numerator1 = Vector3.dotProduct(pe0, normal1) - Vector3.dotProduct(ei0, normal1);
		double denominator1 = Vector3.dotProduct(Vd1, normal1);
		
		if(denominator1 == 0) {
			return null;
		}
		
		double t1 = numerator1 / denominator1;
		Vector3 p1 = new Vector3(ei0.x + t1 * Vd1.x, ei0.y + t1 * Vd1.y, ei0.z + t1 * Vd1.z);
		
		double xMin = ei0.x;
		double yMin = ei0.y;
		double zMin = ei0.z;
		double xMax = ei1.x;
		double yMax = ei1.y;
		double zMax = ei1.z;
		
		if(ei0.x > ei1.x) {
			xMin = ei1.x;
			xMax = ei0.x;
		}
		
		if(ei0.y > ei1.y) {
			yMin = ei1.y;
			yMax = ei0.y;
		}
		
		if(ei0.z > ei1.z) {
			zMin = ei1.z;
			zMax = ei0.z;
		}
		
		if(p1.x < xMin || p1.x > xMax || p1.y < yMin || p1.y > yMax || p1.z < zMin || p1.z > zMax) {
			return null;
		}
		
		//Gets intersection point between next edge position and constructed plane
		Vector3 Vd2 = Vector3.subtract(ef1, ef0).getNormalized();
		double numerator2 = Vector3.dotProduct(pe0, normal1) - Vector3.dotProduct(ef0, normal1);
		double denominator2 = Vector3.dotProduct(Vd2, normal1);
		
		if(denominator2 == 0) {
			return null;
		}
		
		double t2 = numerator2 / denominator2;
		Vector3 p2 = new Vector3(ef0.x + t2 * Vd2.x, ef0.y + t2 * Vd2.y, ef0.z + t2 * Vd2.z);
		
		xMin = ef0.x;
		yMin = ef0.y;
		zMin = ef0.z;
		xMax = ef1.x;
		yMax = ef1.y;
		zMax = ef1.z;
		
		if(ef0.x > ef1.x) {
			xMin = ef1.x;
			xMax = ef0.x;
		}
		
		if(ef0.y > ef1.y) {
			yMin = ef1.y;
			yMax = ef0.y;
		}
		
		if(ef0.z > ef1.z) {
			zMin = ef1.z;
			zMax = ef0.z;
		}
		
		if(p2.x < xMin || p2.x > xMax || p2.y < yMin || p2.y > yMax || p2.z < zMin || p2.z > zMax) {
			return null;
		}
		
		//Gets closest point to one of the intersecting points above
		//This makes it easy to use dot product to determine of the edges passed through each other
		Vector3 Vd3 = Vector3.subtract(pe1, pe0).getNormalized();
		double numerator3 = Vector3.dotProduct(p1, Vd3) - Vector3.dotProduct(pe0, Vd3);
		double denominator3 = Vector3.dotProduct(Vd3, Vd3);
		double t3 = numerator3 / denominator3;
		Vector3 mid = new Vector3(pe0.x + t3 * Vd3.x, pe0.y + t3 * Vd3.y, pe0.z + t3 * Vd3.z);
		
		xMin = pe0.x;
		yMin = pe0.y;
		zMin = pe0.z;
		xMax = pe1.x;
		yMax = pe1.y;
		zMax = pe1.z;
		
		if(pe0.x > pe1.x) {
			xMin = pe1.x;
			xMax = pe0.x;
		}
		
		if(pe0.y > pe1.y) {
			yMin = pe1.y;
			yMax = pe0.y;
		}
		
		if(pe0.z > pe1.z) {
			zMin = pe1.z;
			zMax = pe0.z;
		}
		
		if(mid.x < xMin || mid.x > xMax || mid.y < yMin || mid.y > yMax || mid.z < zMin || mid.z > zMax) {
			return null;
		}
		
		//Dot product determines if edges crossed each other and collided
		double crossed = Vector3.dotProduct(Vector3.subtract(p1, mid), Vector3.subtract(p2, mid));
		if(crossed > 0) {
			return null;
		}
		
		//System.out.println("Edges collided");
		
		//Gets an intersecting point so I can calculate closest point to edge between planes
		Vector3 normal4 = plane1.getNormal();
		Vector3 Vd4 = Vector3.subtract(ef1, ef0).getNormalized();
		double numerator4 = Vector3.dotProduct(plane1.p0, normal4) - Vector3.dotProduct(ef0, normal4);
		double denominator4 = Vector3.dotProduct(Vd4, normal4);
		
		if(denominator4 == 0) {
			return null;
		}
		
		double t4 = numerator4 / denominator4;
		Vector3 intercept1 = new Vector3(ef0.x + t4 * Vd4.x, ef0.y + t4 * Vd4.y, ef0.z + t4 * Vd4.z);
		
		//System.out.println("Intercept: [" + intercept1.x + ", " + intercept1.y + ", " + intercept1.z + "]");
		
		//Gets closest point along edge between planes to intercept
		Vector3 Vd5 = Vector3.subtract(pe1, pe0).getNormalized();
		double numerator5 = Vector3.dotProduct(intercept1, Vd5) - Vector3.dotProduct(pe0, Vd5);
		double denominator5 = Vector3.dotProduct(Vd5, Vd5);
		double t5 = numerator5 / denominator5;
		Vector3 closest = new Vector3(pe0.x + t5 * Vd5.x, pe0.y + t5 * Vd5.y, pe0.z + t5 * Vd5.z);
		
		//Gets closest point along final edge to intercept
		Vector3 Vd6 = Vector3.subtract(ef1, ef0).getNormalized();
		double numerator6 = Vector3.dotProduct(closest, Vd6) - Vector3.dotProduct(ef0, Vd6);
		double denominator6 = Vector3.dotProduct(Vd6, Vd6);
		double t6 = numerator6 / denominator6;
		Vector3 closest2 = new Vector3(ef0.x + t6 * Vd6.x, ef0.y + t6 * Vd6.y, ef0.z + t6 * Vd6.z);
		
		Vector3 displacement = Vector3.subtract(closest, closest2);
		
		return displacement;
	}
	
	public Vector3 OrientedBoxToOrientedBoxCollision2(BoxCollider box1, BoxCollider box2) {
		Vector3 displacement = new Vector3(0, 0, 0);
		
		//Edge to edge test
		int[] lines = {0, 1, 1, 2, 2, 3, 3, 0, 
					   4, 5, 5, 6, 6, 7, 7, 4,
					   4, 0, 1, 5, 2, 6, 3, 7};
		
		for(int i = 0; i < 12; i++) {
			//Top plane and right plane
			Vector3 displacement1 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[0], box2.prev[1], box2.prevPlaneTop);
			if(displacement1 != null && displacement1.getMagnitude() > displacement.getMagnitude()) displacement = displacement1;
			
			//Top plane and left plane
			Vector3 displacement2 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[2], box2.prev[3], box2.prevPlaneTop);
			if(displacement2 != null && displacement2.getMagnitude() > displacement.getMagnitude()) displacement = displacement2;
			
			//Top plane and front plane
			Vector3 displacement3 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[3], box2.prev[0], box2.prevPlaneTop);
			if(displacement3 != null && displacement3.getMagnitude() > displacement.getMagnitude()) displacement = displacement3;
			
			//Top plane and back plane
			Vector3 displacement4 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[1], box2.prev[2], box2.prevPlaneTop);
			if(displacement4 != null && displacement4.getMagnitude() > displacement.getMagnitude()) displacement = displacement4;
			
			//Bottom plane and right plane
			Vector3 displacement5 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[4], box2.prev[5], box2.prevPlaneBottom);
			if(displacement5 != null && displacement5.getMagnitude() > displacement.getMagnitude()) displacement = displacement5;
			
			//Bottom plane and left plane
			Vector3 displacement6 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[6], box2.prev[7], box2.prevPlaneBottom);
			if(displacement6 != null && displacement6.getMagnitude() > displacement.getMagnitude()) displacement = displacement6;
			
			//Bottom plane and front plane
			Vector3 displacement7 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[7], box2.prev[4], box2.prevPlaneBottom);
			if(displacement7 != null && displacement7.getMagnitude() > displacement.getMagnitude()) displacement = displacement7;
			
			//Bottom plane and back plane
			Vector3 displacement8 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[5], box2.prev[6], box2.prevPlaneBottom);
			if(displacement8 != null && displacement8.getMagnitude() > displacement.getMagnitude()) displacement = displacement8;
			
			//Front plane and right plane
			Vector3 displacement9 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[0], box2.prev[4], box2.prevPlaneFront);
			if(displacement9 != null && displacement9.getMagnitude() > displacement.getMagnitude()) displacement = displacement9;
			
			//Front plane and left plane
			Vector3 displacement10 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[3], box2.prev[7], box2.prevPlaneFront);
			if(displacement10 != null && displacement10.getMagnitude() > displacement.getMagnitude()) displacement = displacement10;
			
			//Back plane and right plane
			Vector3 displacement11 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[1], box2.prev[5], box2.prevPlaneBack);
			if(displacement11 != null && displacement11.getMagnitude() > displacement.getMagnitude()) displacement = displacement11;
			
			//Back plane and right plane
			Vector3 displacement12 = edgeToPlaneEdgeCollision2(box1.prev[lines[i * 2]], box1.prev[lines[i * 2 + 1]], 
															  box1.next[lines[i * 2]], box1.next[lines[i * 2 + 1]], 
															  box2.prev[2], box2.prev[6], box2.prevPlaneBack);
			if(displacement12 != null && displacement12.getMagnitude() > displacement.getMagnitude()) displacement = displacement12;
		}
		
		//Corner to plane test
		for(int i = 0; i < 8; i++) {
			//Top face of cube
			Vector3 displacement1 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[0], box2.prev[1], box2.prev[3], false); //1 2 4
			if(displacement1 != null && displacement1.getMagnitude() > displacement.getMagnitude()) displacement = displacement1;
			
			//Bottom face of cube
			Vector3 displacement2 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[4], box2.prev[7], box2.prev[5], false); //5 8 6
			if(displacement2 != null && displacement2.getMagnitude() > displacement.getMagnitude()) displacement = displacement2;
			
			//Left face of cube
			Vector3 displacement3 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[3], box2.prev[2], box2.prev[7], false); //4 3 8
			if(displacement3 != null && displacement3.getMagnitude() > displacement.getMagnitude()) displacement = displacement3;
			
			//Right face of cube
			Vector3 displacement4 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[4], box2.prev[5], box2.prev[0], false); //5 6 1
			if(displacement4 != null && displacement4.getMagnitude() > displacement.getMagnitude()) displacement = displacement4;
			
			//Front face of cube
			Vector3 displacement5 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[4], box2.prev[0], box2.prev[7], false); //5 1 8
			if(displacement5 != null && displacement5.getMagnitude() > displacement.getMagnitude()) displacement = displacement5;
			
			//Back face of cube
			Vector3 displacement6 = lineToPlaneCollision(box1.prev[i], box1.next[i], box2.prev[1], box2.prev[5], box2.prev[2], false); //2 6 3
			if(displacement6 != null && displacement6.getMagnitude() > displacement.getMagnitude()) displacement = displacement6;
		}
		
		if(displacement.getMagnitude() > 0) {
			return displacement;
		}
		
		return null;
	}

}
