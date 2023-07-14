package com.kirby.languagetester.service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

@Service
public class S3Service {

	String bucket_name = "kirbylanguageapp";
	final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();
	

	public PutObjectResult uploadFile(String localFilePath, String awsUploadFilePath) {
		PutObjectResult putObject=null;
		try {
			 putObject = s3.putObject(bucket_name, awsUploadFilePath, new File(localFilePath));
			putObject.getMetadata();
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}
		return putObject;
		
	}

	public boolean getAWSFileURL(String path) {

		 S3Object object=null;
		 try {
			 object=s3.getObject(bucket_name, path);
		 }catch(AmazonS3Exception e) {
			  if (e.getStatusCode() == 404) {
	                System.out.println("Object does not exist in S3.");
	            } else {
	                System.out.println("An error occurred while checking object existence: " + e.getMessage());
	            }
		 }
		 return object!=null;
	}

}
