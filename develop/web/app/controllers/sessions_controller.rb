class SessionsController < ApplicationController
  skip_before_action :verify_authenticity_token, :only => [:create]
  def new
  end

  def create
  	user = User.find_by(name: params[:name].strip)
    if user && user.authenticate(params[:password])
    	render :json => {:state =>'ok', :msg=>'login has beed successd!'}
    else
    	render :json => {:state =>'error', :msg=>'login has beed error!'}
    end
  end

  def destroy
  	log_out
  end
end
