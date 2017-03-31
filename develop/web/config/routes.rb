Rails.application.routes.draw do
  resources :users

  get '/signin', to: 'users#new'
  post  '/signin', to: 'users#create'
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
