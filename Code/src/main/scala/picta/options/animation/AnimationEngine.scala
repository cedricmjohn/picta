package picta.options.animation

import picta.options.Margin

case class AnimationEngine(animation_mode: String = "immediate", transition_duration: Double = 300.0,
                           play_btn_label: String = "Play", pause_btn_label: String = "Pause",
                           btn_pad: Margin = Margin(t=55, r=10), slider_pad: Margin = Margin(l=130, t=55),
                           btn_x: Double = 0.0, btn_y: Double = 0.0, btn_y_anchor: String = "top",
                           btn_x_anchor: String = "left") {

  val play_btn_args = Args(mode = animation_mode, fromcurrent = true, transition = Setting(duration = transition_duration),
    frame=Setting(duration = transition_duration, redraw = true))

  val play_button = PlayButton(method = "animate", args=play_btn_args, label = play_btn_label)

  val pause_btn_args = Args(mode = animation_mode, transition = Setting(duration = 0.0),
    frame=Setting(duration = 0.0, redraw = false))

  val pause_button = PauseButton(method = "animate", args=pause_btn_args, label = pause_btn_label)

  /** can be combined with the layout */
  val update_menus = UpdateMenus(x=btn_x, y=btn_y, yanchor = btn_y_anchor, xanchor = btn_x_anchor,
    showactive = true, direction = "left", menu_type = "buttons", pad = btn_pad, buttons = List(play_button, pause_button))

  /** can be combined with the layout */
  val sliders = Slider(pad = slider_pad) + CurrentValue(visible=true, prefix="Time:", xanchor = "right")
}
