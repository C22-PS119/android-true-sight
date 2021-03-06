package com.truesightid.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.entity.CommentEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.request.*
import com.truesightid.data.source.remote.response.*
import com.truesightid.utils.FilterSearch
import com.truesightid.utils.Resource

interface TrueSightDataSource {
    fun getAllClaims(
        request: GetClaimsRequest,
        filter: FilterSearch?
    ): LiveData<Resource<PagedList<ClaimEntity>>>

    fun loginRequest(loginRequest: LoginRequest): LiveData<ApiResponse<LoginResponse>>

    fun postClaim(postClaimRequest: PostClaimRequest): LiveData<ApiResponse<PostClaimResponse>>

    fun postProfileWithAvatar(editProfileWithAvatarRequest: EditProfileWithAvatarRequest): LiveData<ApiResponse<PostProfileResponse>>

    fun postProfile(editProfileRequest: EditProfileRequest): LiveData<ApiResponse<PostProfileResponse>>

    fun getUserProfile(getProfileRequest: GetProfileRequest): LiveData<ApiResponse<UserResponse>>

    fun registrationRequest(registrationRequest: RegistrationRequest): LiveData<ApiResponse<RegistrationResponse>>

    fun getMyClaims(myDataRequest: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>>

    fun getMyBookmarks(myDataRequest: MyDataRequest): LiveData<ApiResponse<List<ClaimEntity>>>

    fun setPassword(setPasswordRequest: SetPasswordRequest): LiveData<ApiResponse<SetPasswordResponse>>

    fun sendEmailVerification(sendEmailVerificationRequest: SendEmailVerificationRequest): LiveData<ApiResponse<EmailVerificationRespond>>

    fun confirmEmailVerification(confirmEmailVerificationRequest: ConfirmEmailVerificationRequest): LiveData<ApiResponse<ConfirmVerificationRespond>>

    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): LiveData<ApiResponse<SetPasswordResponse>>

    fun getCommentsByClaimId(getCommentsRequest: GetCommentsRequest): LiveData<ApiResponse<List<CommentEntity>>>

    fun upVoteClaimById(api_key: String, id: Int)

    fun downVoteClaimById(api_key: String, id: Int)

    fun addBookmarkById(addRemoveBookmarkRequest: AddRemoveBookmarkRequest)

    fun removeBookmarkById(addRemoveBookmarkRequest: AddRemoveBookmarkRequest)

    fun addCommentById(addCommentRequest: AddCommentRequest)

    fun getClaimsBySearch(claimsRequest: GetClaimsRequest): LiveData<ApiResponse<List<ClaimEntity>>>

    fun deleteClaimById(api_key: String, id: Int, onSuccess: (success:Boolean) -> Unit)

    fun deleteLocalClaimByID(id: Int)

    fun getDeletedClaims(apiKey:String, onFinished: (success: Boolean, deletedClaims: ArrayList<Int>) -> Unit)
}