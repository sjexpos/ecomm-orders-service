package io.oigres.ecomm.service.orders.rpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.oigres.ecomm.service.users.api.UsersService;
import io.oigres.ecomm.service.users.api.model.*;
import io.oigres.ecomm.service.users.api.model.admin.*;
import io.oigres.ecomm.service.users.api.model.consumer.*;
import io.oigres.ecomm.service.users.api.model.dispensary.*;
import io.oigres.ecomm.service.users.api.model.exception.*;
import io.oigres.ecomm.service.users.api.model.exception.profile.ProfileEnableStatusExceptionResponseApi;
import io.oigres.ecomm.service.users.api.model.exception.profile.ProfileException;
import io.oigres.ecomm.service.users.api.model.exception.profile.ProfileNotFoundException;
import io.oigres.ecomm.service.users.api.model.exception.profile.ProfileTypeNotFoundException;
import io.oigres.ecomm.service.users.api.model.profile.ActiveStatusProfileResponse;

public class UsersServiceProxyEnhanced implements UsersService {

    private final UsersService delegate;

    public UsersServiceProxyEnhanced(UsersService delegate) {
        this.delegate = delegate;
    }

    @Override

    public PageResponse<GetAllUsersResponse> getAllUsers(PageableRequest pageable) {
        return delegate.getAllUsers(pageable);
    }

    @Override
    public CreateAdminUserResponse createNewAdminUser(CreateAdminUserRequest request) throws ProfileException, ProfileTypeNotFoundException {
        return delegate.createNewAdminUser(request);
    }

    @Override
    public CreateConsumerUserResponse createNewConsumerUser(CreateConsumerUserRequest request) throws ProfileException, StateNotFoundException, GenderNotFoundException, ZipcodeNotFoundException, UserTypeNotFoundException, ProfileTypeNotFoundException {
        return delegate.createNewConsumerUser(request);
    }

    @Override
    public CreateDispensaryUserResponse createNewDispensaryUser(CreateDispensaryUserRequest request) throws ProfileException, ProfileTypeNotFoundException {
        return delegate.createNewDispensaryUser(request);
    }

    @Override
    @CircuitBreaker(name = "remote-gets", fallbackMethod = "fallbackMethodForGetUserById")
    public GetUserResponse getUserById(Long userId) throws NotFoundException {
        return delegate.getUserById(userId);
    }

    private GetUserResponse fallbackMethodForGetUserById(Exception e) {
        return GetUserResponse.builder()
                .email("Not Available")
                .build();
    }

    @Override
    @CircuitBreaker(name = "remote-gets", fallbackMethod = "fallbackMethodForGetAdminUserById")
    public GetAdminUserResponse getAdminUserById(Long userId) throws NotFoundException {
        return delegate.getAdminUserById(userId);
    }

    private GetAdminUserResponse fallbackMethodForGetAdminUserById(Exception e) {
        return (GetAdminUserResponse) GetAdminUserResponse.builder()
                .email("Not Available")
                .build();
    }

    @Override
    public GetConsumerUserResponse getConsumerUserById(Long userId) throws NotFoundException {
        return delegate.getConsumerUserById(userId);
    }

    @Override
    public GetDispensaryUserResponse getDispensaryUserById(Long userId) throws NotFoundException {
        return delegate.getDispensaryUserById(userId);
    }

    @Override
    public GetDispensaryUserResponse getDispensaryUserByDispensaryId(Long dispensaryId) throws NotFoundException {
        return delegate.getDispensaryUserByDispensaryId(dispensaryId);
    }

    @Override
    public PageResponse<GetAllAdminUsersResponse> getAllAdmins(PageableRequest pageable) {
        return delegate.getAllAdmins(pageable);
    }

    @Override
    public PageResponse<GetAllConsumerUsersResponse> getAllConsumers(PageableRequest pageable) {
        return delegate.getAllConsumers(pageable);
    }

    @Override
    public PageResponse<GetAllDispensaryUsersResponse> getAllDispensaries(PageableRequest pageable) {
        return delegate.getAllDispensaries(pageable);
    }

    @Override
    public UpdateAdminProfileResponse updateAdmin(Long profileId, UpdateAdminProfileRequest request) throws NotFoundException {
        return delegate.updateAdmin(profileId, request);
    }

    @Override
    public UpdateConsumerProfileResponse updateConsumer(Long userId, UpdateConsumerProfileRequest request) throws NotFoundException {
        return delegate.updateConsumer(userId, request);
    }

    @Override
    public UpdateDispensaryProfileResponse updateDispensary(Long userId, UpdateDispensaryProfileRequest request) throws NotFoundException {
        return delegate.updateDispensary(userId, request);
    }

    @Override
    public DeleteUserResponse deleteUserById(Long userId) throws NotFoundException {
        return delegate.deleteUserById(userId);
    }

    @Override
    public DeleteAdminProfileResponse deleteAdminUserById(Long userId) throws NotFoundException {
        return delegate.deleteAdminUserById(userId);
    }

    @Override
    public DeleteConsumerProfileResponse deleteConsumerUserById(Long userId) throws NotFoundException {
        return delegate.deleteConsumerUserById(userId);
    }

    @Override
    public DeleteDispensaryProfileResponse deleteDispensaryUserByDispensaryId(Long dispensaryId) throws NotFoundException {
        return delegate.deleteDispensaryUserByDispensaryId(dispensaryId);
    }

    @Override
    public ActiveStatusProfileResponse activateAdmin(Long userId) throws ProfileException, ProfileTypeNotFoundException, ProfileNotFoundException {
        return delegate.activateAdmin(userId);
    }

    @Override
    public ActiveStatusProfileResponse deactivateAdmin(Long userId) throws ProfileException, ProfileTypeNotFoundException, ProfileNotFoundException {
        return delegate.deactivateAdmin(userId);
    }

    @Override
    public ActiveStatusProfileResponse activateConsumerUser(Long userId) throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        return delegate.activateConsumerUser(userId);
    }

    @Override
    public ActiveStatusProfileResponse deactivateConsumerUser(Long userId) throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        return delegate.deactivateConsumerUser(userId);
    }

    @Override
    public ActiveStatusProfileResponse activateDispensaryUser(Long userId) throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        return delegate.activateDispensaryUser(userId);
    }

    @Override
    public ActiveStatusProfileResponse deactivateDispensaryUser(Long userId) throws ProfileNotFoundException, ProfileEnableStatusExceptionResponseApi {
        return delegate.deactivateDispensaryUser(userId);
    }

    @Override
    public ValidateUserResponse validateUser(ValidateUserRequest request) throws NotFoundException, UnauthorizedException {
        return delegate.validateUser(request);
    }

    @Override
    public void sendCode(SendCodeRequest request) throws NotFoundException, InvalidRequestException {
        delegate.sendCode(request);
    }

    @Override
    public void verifyCode(VerifyCodeRequest request) throws NotFoundException, InvalidRequestException {
        delegate.verifyCode(request);
    }

    @Override
    public PageResponse<GenderResponse> getAllGenders(PageableRequest pageable) throws NotFoundException {
        return delegate.getAllGenders(pageable);
    }

    @Override
    public GenderResponse getGenderById(long genderId) throws GenderNotFoundException {
        return delegate.getGenderById(genderId);
    }

    @Override
    public PageResponse<UserTypeResponse> getAllUserTypes(PageableRequest pageable) {
        return delegate.getAllUserTypes(pageable);
    }

    @Override
    public UserTypeResponse getUserTypeById(long userTypeId) throws UserTypeNotFoundException {
        return delegate.getUserTypeById(userTypeId);
    }

    @Override
    public GetConsumerStateTax getStateTaxByUserId(Long userId) throws NotFoundException {
        return delegate.getStateTaxByUserId(userId);
    }

    @Override
    public UpdateProfileImageResponse changeProfileImageStatus(String imageUrl) {
        return delegate.changeProfileImageStatus(imageUrl);
    }

    @Override
    public ImageUploadLocationResponse getMmjCardImageUploadLocation(String imageExtension) {
        return delegate.getMmjCardImageUploadLocation(imageExtension);
    }

    @Override
    public ImageUploadLocationResponse getAvatarImageUploadLocation(String extension) {
        return delegate.getAvatarImageUploadLocation(extension);
    }

    @Override
    public UpdateCardImageToUploadedStateResponse updateCardImageStatus(String imageUrl) {
        return delegate.updateCardImageStatus(imageUrl);
    }
}
