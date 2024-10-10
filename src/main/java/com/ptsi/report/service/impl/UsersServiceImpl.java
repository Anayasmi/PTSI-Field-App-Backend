package com.ptsi.report.service.impl;

import com.ptsi.report.entity.Users;
import com.ptsi.report.entity.ZoneMaster;
import com.ptsi.report.model.request.LoginRequest;
import com.ptsi.report.model.request.UserRequest;
import com.ptsi.report.model.response.CreationResponse;
import com.ptsi.report.model.response.LoginResponse;
import com.ptsi.report.model.response.UserResponse;
import com.ptsi.report.repository.StaffRepository;
import com.ptsi.report.repository.UsersRepository;
import com.ptsi.report.repository.ZoneRepository;
import com.ptsi.report.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final StaffRepository staffRepository;
    private final ZoneRepository zoneRepository;

    @Override
    public LoginResponse login ( LoginRequest loginRequest ) {
        Users users= usersRepository.findByUserNameAndPassword ( loginRequest.getUserName (), loginRequest.getPassword ( ) ).orElseThrow ();
        List< Map <String,Object> > staffList = staffRepository.findUser(users.getFirstName().trim(),users.getLastName().trim() );
        Integer staffId= (staffList.size() == 0) ? 0 : getIntegerValue ( staffList.get( 0 ).get( "StaffId" ) );

        AtomicReference < String > userRole = new AtomicReference <> ( users.getUserType ( ) );

        if(users.getUserType ().equalsIgnoreCase ( "User" )) {
            List < String > staffIds = zoneRepository.findAll ( ).stream ( ).map ( ZoneMaster :: getProjectCoordinator ).filter ( Objects :: nonNull ).distinct ( ).toList ( );

            for ( String staff : staffIds ) {
                String[] staffs = staff.split ( "," );
                for ( String e : staffs ) {
                    if ( e.equalsIgnoreCase ( String.valueOf ( staffId ) ) ) {
                        userRole.set ( "PRO_CO" );
                        break;
                    }
                }

            }
        }

        return users.toDTO (staffId, userRole.get ( ) );
    }

    @Override
    public CreationResponse updateUserType ( UserRequest userRequest ) {

        Users users = usersRepository.findById ( userRequest.getUserId ( ) ).orElseThrow ();
        users.setUserType ( userRequest.getUserType ( ) );
        users.setUpdatedBy ( userRequest.getUpdatedBy () );
        users.setUpdatedDate ( LocalDateTime.now () );
        usersRepository.save ( users );
        return new CreationResponse(String.valueOf ( users.getUserId () ),"Updated");
    }

    @Override
    public List < UserResponse > fetchActiveUsers ( ) {
        List<Users> users = usersRepository.findByIsLoginActive (-1.0F);
        return users.stream ().map ( Users::toUserDTO ).collect( Collectors.toList());
    }

    Integer getIntegerValue(Object object){
        if(object instanceof Double) {
            return  ( (Double)( object ) ).intValue ();
        }
        if ( object instanceof Float ){
            return Integer.valueOf( String.valueOf( object ) );
        }
        if ( object instanceof Integer ){
            return  ( Integer ) object;
        }
        return 0;
    }
}
