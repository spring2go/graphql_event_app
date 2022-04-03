import React from "react";
import EventItem from "./EventItem/EventItem";
import "./EventList.css";

const eventList = (props) => {
  const events = props.events.map((event) => {
    return (
      <EventItem
        key={event.id}
        eventId={event.id}
        title={event.title}
        price={event.price}
        date={event.date}
        userId={props.authUserId}
        creatorId={event.creator.id}
        onDetail={props.onViewDetail}
      />
    );
  });

  return <ul className="event__list">{events}</ul>;
};

export default eventList;
