/*
GABRIELE: the General Agent Based Repast Implemented Extensible Laboratory for Economics
Copyright (C) 2018  Gianfranco Giulioni
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gabriele.bargaining;

import gabriele.Context;

public class AProductDemand{
	int absoluteRank,relativeRank;
	double demand;
	public AProductDemand(int ar,int rr,double dem){
		absoluteRank=ar;
		relativeRank=rr;
		demand=dem;
	}

	public void inform(int id){
		if(Context.verboseFlag){
			System.out.println("        consumer "+id+" new order of product with absolute rank "+absoluteRank+" and relative rank "+relativeRank+" demanded quantity "+demand);
		}
	}

	public void adjustDemand(int id,double factor){
		demand=demand*factor;
		if(Context.verboseFlag){
			System.out.println("        consumer "+id+" revised demand of product with absolute rank "+absoluteRank+" and relative rank "+relativeRank+" demanded quantity "+demand+" previously demanded "+(demand/factor));
		}
	}

	public double getDemand(){
		return demand;
	}

}
