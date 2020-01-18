import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import WorkIcon from '@material-ui/icons/Work';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import DeleteIcon from '@material-ui/icons/Delete';
import { IconButton } from '@material-ui/core';
import BaseInstance from '../http-client/BaseInstance';
import Cookies from 'js-cookie'
import NotificationsIcon from '@material-ui/icons/Notifications';

export default class NoticesList extends React.Component {
    
    constructor(props) {
        super(props)
    }

    deleteNotice = (noticeId) => {
        BaseInstance.post("deleteNotice", {
            courseId : noticeId
        }).then(res => {
            var actualNotices = this.props.notices
            var updatedNotices = []
            for(let i = 0; i < actualNotices.length; i++) {
                let notice = actualNotices[i]
                if(notice.noticeId !== noticeId)
                    updatedNotices.push(notice)
            }
            
            this.props.setNotices(updatedNotices)
            Cookies.set("courseNotices", updatedNotices)
        })
    }

    render() {
        var array = this.props.notices
        return (
            <div style={{ padding: "10px" }}>
                <Card style={{ maxHeight: 250, overflow: 'auto', width: '90%' }}>
                    <CardContent>
                        {array.length > 0 ? (
                            <List>
                                {
                                    array.map(notice => (
                                        <ListItem>
                                            <ListItemIcon>
                                                <NotificationsIcon />
                                            </ListItemIcon>
                                            <ListItemText primary={`${notice.description}`} /> 
                                            {Cookies.get("lecturerId") === Cookies.get("email") ? (<IconButton onClick={() => this.deleteNotice(notice.noticeId)}><DeleteIcon style={{ color: "#C10000" }} /></IconButton>) : null}
                                        </ListItem>
                                    ))
                                }
                            </List>    
                         ) : ( 
                                <ListItem>
                                    <ListItemText>No Notices Available</ListItemText>
                                </ListItem>
                            )
                        }
                    </CardContent>
                </Card>
            </div>
        );
    }
}