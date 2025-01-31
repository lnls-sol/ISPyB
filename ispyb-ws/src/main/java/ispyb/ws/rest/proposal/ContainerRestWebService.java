package ispyb.ws.rest.proposal;

import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.ws.rest.RestWebService;

import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class ContainerRestWebService extends RestWebService {
	private final static Logger logger = Logger
			.getLogger(ContainerRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/container/{containerIds}/beamline/{beamlines}/samplechangerlocation/update")
	@Produces({ "application/json" })
	public Response updateSampleLocation(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("containerIds") String containerIds,
			@PathParam("beamlines") String beamlines,
			@FormParam("sampleChangerLocation") String sampleChangerLocation) throws NamingException {
		
		long id = this.logInit("updateSampleLocation", logger, token, proposal);
		try {
			
			List<Integer> containerIdList = this.parseToInteger(containerIds);
			List<String> beamlinesList = this.parseToString(beamlines);
			List<String> sampleChangerLocationList = this.parseToString(sampleChangerLocation);
			
			System.out.println("List " + sampleChangerLocationList);
			System.out.println("sampleChangerLocation " + sampleChangerLocation);
			
			for (int i = 0; i < containerIdList.size(); i++) {

				Container3VO container = this.getContainer3Service().findByPk(containerIdList.get(i), false);
				container.setBeamlineLocation(beamlinesList.get(i));
				if (sampleChangerLocationList.get(i) != null){
					container.setSampleChangerLocation(sampleChangerLocationList.get(i));
				}
				else{
					container.setSampleChangerLocation(null);
				}
				this.getContainer3Service().update(container);
			}
			
			this.logFinish("updateSampleLocation", id, logger);
			HashMap<String, String> response = new HashMap<String, String>();
			response.put("updateSampleLocation", "ok");
			return sendResponse(response);
		} catch (Exception e) {
			return this.logError("updateSampleLocation", e, id, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/container/{containerIds}/samplechangerlocation/empty")
	@Produces({ "application/json" })
	public Response emptySampleLocation(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("containerIds") String containerIds
			) throws NamingException {
		
		long id = this.logInit("emptySampleLocation", logger, token, proposal, containerIds);
		try {
			List<Integer> containerIdList = this.parseToInteger(containerIds);
			for (int i = 0; i < containerIdList.size(); i++) {
				Container3VO container = this.getContainer3Service().findByPk(containerIdList.get(i), false);
				container.setSampleChangerLocation(null);
				container.setBeamlineLocation("");
				this.getContainer3Service().update(container);
			}			
			this.logFinish("emptySampleLocation", id, logger);
			return sendResponse(Response.ok());
		} catch (Exception e) {
			return this.logError("emptySampleLocation", e, id, logger);
		}
	}
	
		

}
