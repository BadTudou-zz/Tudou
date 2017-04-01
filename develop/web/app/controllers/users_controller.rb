class UsersController < ApplicationController
	skip_before_action :verify_authenticity_token, :only => [:create]
	#the Web page of register
	def new
		render plain: 'get Web page of register'
	end

	#REST:create
	def create
		@user = User.new(user_params)
 		if @user.save
 			log_in user
 			render :json => {:state =>'ok', :msg=>'user has beed create successd!'}
 		else
 			print 'error'
 			render :json => {:state =>'error', :msg=>'user/email/phone has been used!', error:@user.errors.full_messages}
 		end
	end

	#REST:read
	def show
		#render plain: 'get Web page of show'
		@user = User.find(params[:id])
	end

	#the Web page of edit
	def edit
	end

	#REST:update
	def update
	end

	#REST:delete
	def destroy
	end


  private

    def user_params
      params.permit([:name, :phone, :email, :password])
    end

end
